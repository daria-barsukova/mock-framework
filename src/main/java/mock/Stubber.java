package mock;

public class Stubber<T> {

    public void thenReturn(T retObj) {
        MockContext.getLastMockInvocationHandler().setRetObj(retObj);
    }

    public void thenThrow(Throwable throwable) {
        MockContext.getLastMockInvocationHandler().setThrowable(throwable);
    }

    public void invokeRealMethod() {
        MockContext.getLastMockInvocationHandler().setRealMethodInvocation();
    }
}
