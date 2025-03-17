package mock.core;

import java.lang.reflect.Proxy;
import java.lang.reflect.Field;

import mock.invocation.DelegationStrategy;
import mock.invocation.MockInvocationHandler;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

public class Create {

    public static <T> T mock(Class<T> classToMock, DelegationStrategy delegationStrategy) {
        MockContext.setLastMockInvocationHandler(new MockInvocationHandler(delegationStrategy));

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
                MockContext.getLastMockInvocationHandler()
        );
    }

    private static <T> T mockObject(Class<T> classToMock) {
        try {
            Class<? extends T> byteBuddy = new ByteBuddy()
                    .subclass(classToMock)
                    .method(ElementMatchers.any().and(ElementMatchers.not(ElementMatchers.named("clone"))))
                    .intercept(InvocationHandlerAdapter.of(MockContext.getLastMockInvocationHandler()))
                    .make()
                    .load(classToMock.getClassLoader())
                    .getLoaded();

            Objenesis objenesis = new ObjenesisStd();
            ObjectInstantiator<? extends T> instantiator = objenesis.getInstantiatorOf(byteBuddy);
            return instantiator.newInstance();

        } catch (Exception e) {
            throw new RuntimeException("Failed to create mock for class: " + classToMock.getName(), e);
        }
    }
    public static <T> T spy(T obj) {
        return spy(obj, DelegationStrategy.CALL_REAL_METHOD);
    }

    public static <T> T spy(T obj, DelegationStrategy delegationStrategy) {
        MockContext.setLastMockInvocationHandler(new MockInvocationHandler(delegationStrategy));

        try {
            Class<? extends T> byteBuddy = new ByteBuddy()
                    .subclass((Class<T>) obj.getClass())
                    .method(ElementMatchers.any().and(ElementMatchers.not(ElementMatchers.named("clone"))))
                    .intercept(InvocationHandlerAdapter.of(MockContext.getLastMockInvocationHandler()))
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
