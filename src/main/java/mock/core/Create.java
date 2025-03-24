package mock.core;

import java.lang.reflect.*;
import java.util.List;

import mock.invocation.StaticMethodHandler;
import mock.invocation.DelegationStrategy;
import mock.invocation.MockInvocationHandler;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

import java.util.*;
import java.util.stream.Collectors;

import static mock.invocation.DelegationStrategy.RETURN_DEFAULT;


public class Create {

    public static <T> T mock(Class<T> classToMock, DelegationStrategy delegationStrategy) {
        MockContext.setLastMockInvocationHandler(new MockInvocationHandler(delegationStrategy));

        if (classToMock.isInterface()) {
             return null;
        } else {

            return mockObject(classToMock);
        }


    }
//    @SuppressWarnings("unchecked")
//    private static <T> T mockInterface(Class<T> classToMock) {
//        return (T) Proxy.newProxyInstance(
//                classToMock.getClassLoader(),
//                new Class<?>[]{classToMock},
//                MockContext.getLastMockInvocationHandler()
//        );
//    }


    private static <T> T mockObject(Class<T> classToMock) {
        MockContext.setLastMockInvocationHandler(new MockInvocationHandler(RETURN_DEFAULT));

        Class<? extends T> byteBuddy = new ByteBuddy()
                .subclass(classToMock)
                .method(ElementMatchers.any())
                .intercept(MethodDelegation.to(MockContext.getLastMockInvocationHandler()))
                .make()
                .load(ClassLoader.getSystemClassLoader())
                .getLoaded();

        Objenesis objenesis = new ObjenesisStd();
        ObjectInstantiator<? extends T> thingyInstantiator = objenesis.getInstantiatorOf(byteBuddy);
        return thingyInstantiator.newInstance();
    }

    public static <T> T spy(T obj) {
        return spy(obj, DelegationStrategy.CALL_REAL_METHOD);
    }

    public static <T> T spy(T obj, DelegationStrategy delegationStrategy) {
        MockContext.setLastMockInvocationHandler(new MockInvocationHandler(delegationStrategy));


        Class<? extends T> byteBuddy = new ByteBuddy()
                .subclass((Class<T>) obj.getClass())
                .method(ElementMatchers.any())
                .intercept(MethodDelegation.to(MockContext.getLastMockInvocationHandler()))
                .make()
                .load(ClassLoader.getSystemClassLoader())
                .getLoaded();

        Objenesis objenesis = new ObjenesisStd();
        ObjectInstantiator<? extends T> thingyInstantiator = objenesis.getInstantiatorOf(byteBuddy);
        T instance = thingyInstantiator.newInstance();


        copyFields(obj, instance);
        return instance;

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

    public static <T> StaticStubber<T> mockStatic(Class<T> clazz) {
        MockInvocationHandler staticHandler = new MockInvocationHandler(RETURN_DEFAULT);
        MockContext.setStaticMockHandler(clazz, staticHandler);
        MockContext.setLastMockInvocationHandler(staticHandler);

        System.out.println("Static mock handler registered successfully for " + clazz.getName());

        StaticMethodHandler.lastMockInvocationHandler = staticHandler;

        ByteBuddyAgent.install();
        List<Method> staticMethods = getStaticMethodsOfClass(clazz);

        ByteBuddy byteBuddy = new ByteBuddy();
        DynamicType.Builder<T> builder = byteBuddy.redefine(clazz);

        for (Method method : staticMethods) {
            builder = builder.visit(Advice.to(StaticMethodHandler.class).on(ElementMatchers.is(method)));
        }

        try (DynamicType.Unloaded<T> made = builder.make()) {
            made.load(clazz.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
        }

        MockContext.setStaticIntercept(true, clazz);
        System.out.println("Static intercept enabled for: " + clazz.getName());

        return new StaticStubber<>(clazz, staticHandler);
    }

    private static <T> List<Method> getStaticMethodsOfClass(Class<T> tClass) {
        return Arrays.stream(tClass.getDeclaredMethods())
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .collect(Collectors.toList());
    }
}
