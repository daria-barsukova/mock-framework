package mock;

import java.lang.reflect.Proxy;

public class Mock {
    public static <T> T mock(Class<T> classToMock) {
        if (classToMock.isInterface()) {
            return mockInterface(classToMock);
        }
        else {
            return null;
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
}
