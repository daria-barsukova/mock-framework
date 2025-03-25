package mock.invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

import mock.core.MockContext;
import mock.matchers.ArgumentMatchers;
import mock.matchers.Matcher;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

public class MockInvocationHandler {
    private final List<InvocationConfig> invocationConfigs = new ArrayList<>();
    private Method lastMethod = null;
    private List<Matcher<?>> lastMatchers = null;
    private final DelegationStrategy delegationStrategy;
    private boolean interceptStaticMethods = false;

    public MockInvocationHandler(DelegationStrategy delegationStrategy) {
        this.delegationStrategy = delegationStrategy;
    }

    @RuntimeType
    public Object invoke(@SuperCall Callable<?> zuper, @Origin Method method, @AllArguments Object[] args) throws Throwable {
        this.lastMethod = method;
        this.lastMatchers = ArgumentMatchers.captureMatchers();
        System.out.println(lastMatchers);
        MockContext.setLastMockInvocationHandler(this);

        if (interceptStaticMethods && java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
            System.out.println("Mocked static method: " + method.getName());
        } else {
            System.out.println("Mocked method: " + method.getName());
        }

        for (InvocationConfig config : invocationConfigs) {
//            System.out.println("Checking stored config: " + config.getMethod().getName() +
//                    ", Expected args: " + Arrays.toString(config.getArgs()) +
//                    ", Stored return: " + config.getRetObj());
            if (config.getMethod().equals(method) && matchArgs(args, config.getMatchers())) {
                switch (config.getDelegationStrategy()) {
                    case CALL_REAL_METHOD -> {
                        return zuper.call();
                    }
                    case RETURN_CUSTOM -> {
                        return config.getRetObj();
                    }
                    case RETURN_THROW -> {
                        throw config.getToThrow();
                    }
                }
            }
        }

        switch (delegationStrategy) {
            case CALL_REAL_METHOD -> {
                if (interceptStaticMethods && java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                    throw new UnsupportedOperationException("Cannot call real method on static mocks.");
                }
                return zuper.call();
            }
            case RETURN_DEFAULT -> {
                return getDefaultReturnValue(method.getReturnType());
            }
            default -> throw new UnsupportedOperationException("Unknown delegation strategy: " + delegationStrategy);
        }
    }

    @SuppressWarnings("unchecked")
    public boolean matchArgs(Object[] args, List<Matcher<?>> configMatchers) {
        if (Objects.isNull(args) && configMatchers.isEmpty()) {
            return true;
        }
        if (configMatchers.size() != args.length) return false;
        for (int i = 0; i < args.length; i++) {
            Matcher matcher = configMatchers.get(i);
            Object actualArg = args[i];
            if (!matcher.matches(actualArg)) {
                return false;
            }
        }
        return true;
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
                lastMatchers.equals(invocationConfig.getMatchers()));
        invocationConfigs.add(new InvocationConfig(lastMethod, lastMatchers, retObj));
    }

    public void setRealMethodInvocation() {
        invocationConfigs.removeIf(invocationConfig -> invocationConfig.getMethod().equals(lastMethod) &&
                lastMatchers.equals(invocationConfig.getMatchers()));
        invocationConfigs.add(new InvocationConfig(lastMethod, lastMatchers));
    }

    public void setThrowable(Throwable throwable) {
        invocationConfigs.removeIf(invocationConfig -> invocationConfig.getMethod().equals(lastMethod) &&
                lastMatchers.equals(invocationConfig.getMatchers()));
        invocationConfigs.add(new InvocationConfig(lastMethod, lastMatchers, throwable));
    }

    public void setInterceptStaticMethods(boolean intercept) {
        this.interceptStaticMethods = intercept;
    }

    public boolean isInterceptStaticMethods() {
        return interceptStaticMethods;
    }
}
