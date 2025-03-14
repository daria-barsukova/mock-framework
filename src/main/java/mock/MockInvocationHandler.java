package mock;

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

    public MockInvocationHandler(DelegationStrategy delegationStrategy) {
        this.delegationStrategy = delegationStrategy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Mocked method: " + method.getName());
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
        invocationConfigs.removeIf(invocationConfig -> invocationConfig.getMethod().equals(lastMethod) && Arrays.deepEquals(invocationConfig.getArgs(), lastArgs));
        invocationConfigs.add(new InvocationConfig(lastMethod, lastArgs, throwable));
    }
}
