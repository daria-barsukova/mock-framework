package mock.invocation;

import java.lang.reflect.Method;

class InvocationConfig {
    private final Object[] args;
    private final Method method;
    private final Object retObj;
    private final DelegationStrategy delegationStrategy;
    private final Throwable toThrow;

    InvocationConfig(Method method, Object[] args, Object retObj) {
        this.args = args;
        this.method = method;
        this.retObj = retObj;
        this.delegationStrategy = DelegationStrategy.RETURN_CUSTOM;
        this.toThrow = null;
    }

    InvocationConfig(Method method, Object[] args) {
        this.args = args;
        this.method = method;
        this.retObj = null;
        this.delegationStrategy = DelegationStrategy.CALL_REAL_METHOD;
        this.toThrow = null;

    }

    InvocationConfig(Method method, Object[] args, Throwable toThrow) {
        this.args = args;
        this.method = method;
        this.retObj = null;
        this.delegationStrategy = DelegationStrategy.RETURN_THROW;
        this.toThrow = toThrow;

    }

    public Object[] getArgs() {
        return args;
    }

    public Method getMethod() {
        return method;
    }

    public Throwable getToThrow() {
        return toThrow;
    }

    public Object getRetObj() {
        return retObj;
    }

    public DelegationStrategy getDelegationStrategy() {
        return delegationStrategy;
    }
}
