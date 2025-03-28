package mock.core;

import mock.invocation.StaticMethodHandler;
import mock.invocation.MockInvocationHandler;

import java.util.HashMap;
import java.util.Map;

public class MockContext {

    private static final Map<Class<?>, MockInvocationHandler> staticMocks = new HashMap<>();
    private static MockInvocationHandler lastMockInvocationHandler;

    public static MockInvocationHandler getLastMockInvocationHandler() {
        return lastMockInvocationHandler;
    }

    public static void setLastMockInvocationHandler(MockInvocationHandler handler) {
        lastMockInvocationHandler = handler;
    }

    public static void setStaticMockHandler(Class<?> clazz, MockInvocationHandler handler) {
        if (clazz == null || handler == null) {
            throw new IllegalArgumentException("Class and handler must not be null");
        }
        staticMocks.put(clazz, handler);
    }

    public static MockInvocationHandler getStaticMockHandler(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class must not be null");
        }
        return staticMocks.get(clazz);
    }

    public static void removeStaticMock(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class must not be null");
        }
        staticMocks.remove(clazz);
        StaticMethodHandler.lastMockInvocationHandler = null;
    }

    public static void setStaticIntercept(boolean intercept, Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class must not be null");
        }
        MockInvocationHandler handler = getStaticMockHandler(clazz);
        if (handler != null) {
            handler.setInterceptStaticMethods(intercept);
        } else {
            throw new IllegalStateException("No handler found for: " + clazz.getName() + ". Ensure mockStatic() was called before");
        }
    }
}
