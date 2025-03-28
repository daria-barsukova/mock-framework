package mock.invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import mock.core.MockContext;
import mock.matchers.ArgumentMatchers;
import mock.matchers.Matcher;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

public class MockInvocationHandler implements InvocationHandler {

    private final List<InvocationConfig> invocationConfigs = new ArrayList<>();
    private Method lastMethod = null;
    private List<Matcher<?>> lastMatchers = null;
    private final DelegationStrategy delegationStrategy;
    private boolean interceptStaticMethods = false;
    private final boolean isInterface;

    public MockInvocationHandler(DelegationStrategy delegationStrategy, boolean isInterface) {
        this.delegationStrategy = delegationStrategy;
        this.isInterface = isInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!isInterface) {
            return handleInvocation((Callable<?>) proxy, method, args);
        }
        this.lastMethod = method;
        this.lastMatchers = ArgumentMatchers.captureMatchers();

        for (InvocationConfig config : invocationConfigs) {
            if (config.getMethod().equals(method) && matchArgs(args, config.getMatchers())) {
                switch (config.getDelegationStrategy()) {
                    case RETURN_CUSTOM -> {
                        return config.getRetObj();
                    }
                    case RETURN_THROW -> throw config.getToThrow();
                }
            }
        }
        return getDefaultReturnValue(method.getReturnType());
    }

    @RuntimeType
    public Object handleInvocation(@SuperCall Callable<?> zuper, @Origin Method method, @AllArguments Object[] args) throws Throwable {
        this.lastMethod = method;
        this.lastMatchers = ArgumentMatchers.captureMatchers();
        MockContext.setLastMockInvocationHandler(this);

        for (InvocationConfig config : invocationConfigs) {
            if (config.getMethod().equals(method) && matchArgs(args, config.getMatchers())) {
                switch (config.getDelegationStrategy()) {
                    case CALL_REAL_METHOD -> {
                        return zuper.call();
                    }
                    case RETURN_CUSTOM -> {
                        return config.getRetObj();
                    }
                    case RETURN_THROW -> throw config.getToThrow();
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
        if (args == null && configMatchers.isEmpty()) {
            return true;
        }
        if (configMatchers.size() != args.length) {
            return false;
        }
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
            return switch (returnType.getName()) {
                case "boolean" -> false;
                case "int" -> 0;
                case "long" -> 0L;
                case "double" -> 0.0;
                case "float" -> 0.0f;
                case "char" -> '\u0000';
                case "byte" -> (byte) 0;
                case "short" -> (short) 0;
                default -> null;
            };
        }
        return null;
    }

    private void updateInvocationConfig(InvocationConfig newConfig) {
        if (lastMethod == null || lastMatchers == null) {
            throw new IllegalStateException("No method or matchers available for invocation configuration.");
        }
        invocationConfigs.removeIf(invocationConfig ->
                invocationConfig.getMethod().equals(lastMethod) &&
                        lastMatchers.equals(invocationConfig.getMatchers())
        );
        invocationConfigs.add(newConfig);
    }

    public void setRetObj(Object retObj) {
        updateInvocationConfig(
                new InvocationConfig.Builder(lastMethod, lastMatchers)
                        .returnObject(retObj)
                        .build()
        );
    }

    public void setRealMethodInvocation() {
        updateInvocationConfig(
                new InvocationConfig.Builder(lastMethod, lastMatchers)
                        .build()
        );
    }

    public void setThrowable(Throwable throwable) {
        updateInvocationConfig(
                new InvocationConfig.Builder(lastMethod, lastMatchers)
                        .throwException(throwable)
                        .build()
        );
    }

    public void setInterceptStaticMethods(boolean intercept) {
        this.interceptStaticMethods = intercept;
    }

    public boolean isInterceptStaticMethods() {
        return interceptStaticMethods;
    }
}
