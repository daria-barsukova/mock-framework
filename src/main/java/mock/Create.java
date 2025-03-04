package mock;

import java.lang.reflect.Proxy;
import java.lang.reflect.Field;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

public class Create {

    public static <T> T mock(Class<T> classToMock) {
        if (classToMock.isInterface()) {
            return mockInterface(classToMock);
        } else {
            return mockObject(classToMock);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T mockInterface(Class<T> classToMock) {
        return (T) Proxy.newProxyInstance(
                classToMock.getClassLoader(),
                new Class<?>[]{classToMock},
                new MockInvocationHandler()
        );
    }

    private static <T> T mockObject(Class<T> classToMock) {
        try {
            return new ByteBuddy()
                    .subclass(classToMock)
                    .method(ElementMatchers.any())
                    .intercept(InvocationHandlerAdapter.of(new MockInvocationHandler()))
                    .make()
                    .load(classToMock.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create mock for class: " + classToMock.getName(), e);
        }
    }

    public static <T> T spy(T obj) {
        try {
            Class<? extends T> byteBuddy = new ByteBuddy()
                    .subclass((Class<T>) obj.getClass())
                    .method(ElementMatchers.any())
                    .intercept(InvocationHandlerAdapter.of(new MockInvocationHandler()))
                    .make()
                    .load(obj.getClass().getClassLoader())
                    .getLoaded();

            Objenesis objenesis = new ObjenesisStd();
            ObjectInstantiator<? extends T> instantiator = objenesis.getInstantiatorOf(byteBuddy);
            T instance = instantiator.newInstance();

            copyFields(obj, instance);

            return instance;

        } catch (Exception e) {
            throw new RuntimeException("Failed to create spy for object: " + obj.getClass().getName(), e);
        }
    }

    private static <T> void copyFields(T source, T target) {
        Field[] fields = source.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(source);
                if (value != null) {
                    Field targetField = target.getClass().getSuperclass().getDeclaredField(field.getName());
                    targetField.setAccessible(true);
                    targetField.set(target, value);
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException("Failed to copy field: " + field.getName(), e);
            }
        }
    }
}
