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

    private InvocationConfig(Builder builder) {
        this.matchers = builder.matchers;
        this.method = builder.method;
        this.retObj = builder.retObj;
        this.delegationStrategy = builder.delegationStrategy;
        this.toThrow = builder.toThrow;
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

    public static class Builder {
        private final Method method;
        private final List<Matcher<?>> matchers;
        private Object retObj = null;
        private DelegationStrategy delegationStrategy = DelegationStrategy.CALL_REAL_METHOD;
        private Throwable toThrow = null;

        public Builder(Method method, List<Matcher<?>> matchers) {
            this.method = method;
            this.matchers = matchers;
        }

        public Builder returnObject(Object retObj) {
            this.retObj = retObj;
            this.delegationStrategy = DelegationStrategy.RETURN_CUSTOM;
            return this;
        }

        public Builder throwException(Throwable toThrow) {
            this.toThrow = toThrow;
            this.delegationStrategy = DelegationStrategy.RETURN_THROW;
            return this;
        }

        public InvocationConfig build() {
            return new InvocationConfig(this);
        }
    }
}
