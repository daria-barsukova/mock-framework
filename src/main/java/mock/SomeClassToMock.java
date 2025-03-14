package mock;

public class SomeClassToMock {
    public void method() {
        System.out.println("default impl");
    }

    public String methodReturningValue() {
        return "Original result";
    }
}
