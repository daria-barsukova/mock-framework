package mock;

public class Main {
    public static void main(String[] args) {
        SomeInterface someInterface = Mock.mock(SomeInterface.class);
        someInterface.doSmth();
    }
}

interface SomeInterface {
    void doSmth();
}
