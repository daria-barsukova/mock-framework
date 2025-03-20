package mock.core;

import mock.annotations.MockAnnotation;
import mock.invocation.DelegationStrategy;
import mock.invocation.MockInvocationHandler;

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
        MockInvocationHandler handler = MockContext.getLastMockInvocationHandler();
        if (handler == null) {
            throw new IllegalStateException("No mock handler found. Ensure mockStatic() was called before when()");
        }
        return new Stubber<>(handler);
    }

    public static StaticStubber mockStatic(Class<?> clazz) {
        return Create.mockStatic(clazz);
    }
}
