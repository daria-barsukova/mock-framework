package mock;

import java.lang.reflect.Proxy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

public class Mock {
    public static <T> T mock(Class<T> classToMock) {
        if (classToMock.isInterface()) {
            return mockInterface(classToMock);
        }
        else {
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
                    .method(ElementMatchers.named("method"))
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
}
