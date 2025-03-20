package mock;

public class SomeClassToMock {
    public void method() {
        System.out.println("default impl");
    }

    public String methodReturningValue() {
        return "Original result";
    }

    public String someMethod(String str) {
        return "Default";
    }

    public static String staticMethod(String str) {
        return "Real Static Result";
    }
}
