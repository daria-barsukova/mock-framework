package mock;

public class Main {
    public static void main(String[] args) {
        SomeInterface someInterface = Mock.mock(SomeInterface.class);
        someInterface.doSmth();

        SomeClassToMock someClass = Mock.mock(SomeClassToMock.class);
        someClass.method();
    }
}

interface SomeInterface {
    void doSmth();
}