package mock.core;

import mock.annotations.MockAnnotation;
import mock.invocation.DelegationStrategy;

public class SuperMock {

    public static <T> T mock(Class<T> classToMock) {
        return Create.mock(classToMock, DelegationStrategy.RETURN_DEFAULT);
    }

    public static <T> T spy(Class<T> classToSpy) {
        return Create.mock(classToSpy, DelegationStrategy.CALL_REAL_METHOD);
    }

    public static <T> T spy(T obj) {
        return Create.spy(obj);
    }

    public static void initMocks(Object instance) {
        MockAnnotation.initMocks(instance);
    }

    public static <T> Stubber<T> when(T obj) {
        return new Stubber<>();
    }
}
