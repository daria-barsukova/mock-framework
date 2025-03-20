package mock.invocation;

import java.lang.reflect.Method;
import java.util.List;

import mock.matchers.Matcher;

class InvocationConfig {
    private final List<Matcher<?>> matchers;
    private final Method method;
    private final Object retObj;
    private final DelegationStrategy delegationStrategy;
    private final Throwable toThrow;

    InvocationConfig(Method method, List<Matcher<?>> matchers, Object retObj) {
        this.matchers = matchers;
        this.method = method;
        this.retObj = retObj;
        this.delegationStrategy = DelegationStrategy.RETURN_CUSTOM;
        this.toThrow = null;
    }

    InvocationConfig(Method method, List<Matcher<?>> matchers) {
        this.matchers = matchers;
        this.method = method;
        this.retObj = null;
        this.delegationStrategy = DelegationStrategy.CALL_REAL_METHOD;
        this.toThrow = null;

    }

    InvocationConfig(Method method, List<Matcher<?>> matchers, Throwable toThrow) {
        this.matchers = matchers;
        this.method = method;
        this.retObj = null;
        this.delegationStrategy = DelegationStrategy.RETURN_THROW;
        this.toThrow = toThrow;

    }

    public List<Matcher<?>> getMatchers() {
        return matchers;
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
