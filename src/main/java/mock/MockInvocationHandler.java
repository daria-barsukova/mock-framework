package mock;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MockInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        System.out.println("Mocked method: " + method.getName());
        return null;
    }
}
