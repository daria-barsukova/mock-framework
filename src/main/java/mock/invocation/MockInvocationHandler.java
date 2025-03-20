package mock.invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MockInvocationHandler implements InvocationHandler {
    private final List<InvocationConfig> invocationConfigs = new ArrayList<>();
    private Method lastMethod = null;
    private Object[] lastArgs = null;
    private final DelegationStrategy delegationStrategy;
    private boolean interceptStaticMethods = false;

    public MockInvocationHandler(DelegationStrategy delegationStrategy) {
        this.delegationStrategy = delegationStrategy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        this.lastMethod = method;
        this.lastArgs = args;

        if (interceptStaticMethods && java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
            System.out.println("Mocked static method: " + method.getName());
        } else {
            System.out.println("Mocked method: " + method.getName());
        }

        for (InvocationConfig config : invocationConfigs) {
//            System.out.println("Checking stored config: " + config.getMethod().getName() +
//                    ", Expected args: " + Arrays.toString(config.getArgs()) +
//                    ", Stored return: " + config.getRetObj());
            if (config.getMethod().equals(method) && Arrays.deepEquals(config.getArgs(), args)) {
                switch (config.getDelegationStrategy()) {
                    case RETURN_CUSTOM -> {
                        return config.getRetObj();
                    }
                    case RETURN_THROW -> {
                        return config.getToThrow();
                    }
                }
            }
        }

        switch (delegationStrategy) {
            case CALL_REAL_METHOD -> {
                if (interceptStaticMethods && java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                    throw new UnsupportedOperationException("Cannot call real method on static mocks.");
                }
                return method.invoke(proxy, args);
            }
            case RETURN_DEFAULT -> {
                return getDefaultReturnValue(method.getReturnType());
            }
            default -> throw new UnsupportedOperationException("Unknown delegation strategy: " + delegationStrategy);
        }
    }

    private Object getDefaultReturnValue(Class<?> returnType) {
        if (returnType.isPrimitive()) {
            if (returnType.equals(boolean.class)) {
                return false;
            } else if (returnType.equals(int.class)) {
                return 0;
            } else if (returnType.equals(long.class)) {
                return 0L;
            } else if (returnType.equals(double.class)) {
                return 0.0;
            } else if (returnType.equals(float.class)) {
                return 0.0f;
            } else if (returnType.equals(char.class)) {
                return '\u0000';
            } else if (returnType.equals(byte.class)) {
                return (byte) 0;
            } else if (returnType.equals(short.class)) {
                return (short) 0;
            }
        }
        return null;
    }

    public void setRetObj(Object retObj) {
        invocationConfigs.removeIf(invocationConfig -> invocationConfig.getMethod().equals(lastMethod) &&
                Arrays.deepEquals(invocationConfig.getArgs(), lastArgs));
        invocationConfigs.add(new InvocationConfig(lastMethod, lastArgs, retObj));
    }

    public void setRealMethodInvocation() {
        invocationConfigs.removeIf(invocationConfig -> invocationConfig.getMethod().equals(lastMethod) &&
                Arrays.deepEquals(invocationConfig.getArgs(), lastArgs));
        invocationConfigs.add(new InvocationConfig(lastMethod, lastArgs));
    }

    public void setThrowable(Throwable throwable) {
        invocationConfigs.removeIf(invocationConfig -> invocationConfig.getMethod().equals(lastMethod) &&
                Arrays.deepEquals(invocationConfig.getArgs(), lastArgs));
        invocationConfigs.add(new InvocationConfig(lastMethod, lastArgs, throwable));
    }

    public void setInterceptStaticMethods(boolean intercept) {
        this.interceptStaticMethods = intercept;
    }

    public boolean isInterceptStaticMethods() {
        return interceptStaticMethods;
    }
}
