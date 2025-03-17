package mock.core;

import mock.invocation.MockInvocationHandler;

public class MockContext {
    private static MockInvocationHandler lastMockInvocationHandler;

    public static MockInvocationHandler getLastMockInvocationHandler() {
        return lastMockInvocationHandler;
    }

    public static void setLastMockInvocationHandler(MockInvocationHandler handler) {
        lastMockInvocationHandler = handler;
    }
}
