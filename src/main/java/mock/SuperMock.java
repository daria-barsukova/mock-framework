package mock;

public class SuperMock {

    public static <T> T mock(Class<T> classToMock) {
        return Create.mock(classToMock);
    }

    public static <T> T spy(T obj) {
        return Create.spy(obj);
    }

    public static void initMocks(Object instance) {
        MockAnnotation.initMocks(instance);
    }
}
