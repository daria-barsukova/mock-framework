package mock.core;

import mock.invocation.MockInvocationHandler;

public class StaticStubber<T> implements AutoCloseable {

    private final Class<T> originalClass;
    private final MockInvocationHandler handler;

    public StaticStubber(Class<T> classToMock, MockInvocationHandler handler) {
        this.originalClass = classToMock;
        this.handler = handler;

        if (handler == null) {
            throw new IllegalStateException("No static handler found for: " + classToMock.getName());
        }
    }

    public <S> Stubber<S> when(S methodCall) {
        return new Stubber<>(handler);
    }

    private void restoreOriginalClass() {
        MockContext.setStaticIntercept(false, originalClass);
        MockContext.removeStaticMock(originalClass);
    }

    @Override
    public void close() {
        restoreOriginalClass();
    }
}
