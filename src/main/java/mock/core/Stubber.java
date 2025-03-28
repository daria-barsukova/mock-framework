package mock.core;

import mock.invocation.MockInvocationHandler;

public class Stubber<T> {

    private final MockInvocationHandler handler;

    public Stubber(MockInvocationHandler handler) {
        this.handler = handler;
    }

    public void thenReturn(T retObj) {
        if (handler == null) {
            throw new IllegalStateException("No MockInvocationHandler available for thenReturn");
        }
        handler.setRetObj(retObj);
    }

    public void thenThrow(Throwable throwable) {
        if (handler == null) {
            throw new IllegalStateException("No MockInvocationHandler available for thenThrow");
        }
        handler.setThrowable(throwable);
    }

    public void invokeRealMethod() {
        if (handler == null) {
            throw new IllegalStateException("No MockInvocationHandler available for invokeRealMethod");
        }
        handler.setRealMethodInvocation();
    }
}
