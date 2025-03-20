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
        staticMocks.put(clazz, handler);
    }

    public static MockInvocationHandler getStaticMockHandler(Class<?> clazz) {
        return staticMocks.get(clazz);
    }

    public static void removeStaticMock(Class<?> clazz) {
        if (staticMocks.remove(clazz) != null) {
            System.out.println("Static mock removed for: " + clazz.getName());
        }
        StaticMethodHandler.lastMockInvocationHandler = null;
    }

    public static void setStaticIntercept(boolean intercept, Class<?> clazz) {
        MockInvocationHandler handler = getStaticMockHandler(clazz);
        if (handler != null) {
            System.out.println("Setting static intercept for: " + clazz.getName() + " to " + intercept);
            handler.setInterceptStaticMethods(intercept);
        } else {
            System.out.println("No handler found for: " + clazz.getName() + ". Ensure mockStatic() was called before");
        }
    }
}
