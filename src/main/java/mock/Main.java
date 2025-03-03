package mock;

public class Main {
    public static void main(String[] args) {

        SomeInterface someInterface = SuperMock.mock(SomeInterface.class);
        someInterface.doSmth();

        SomeClassToMock someClass = SuperMock.mock(SomeClassToMock.class);
        someClass.method();

        SomeClassToMock realObject = new SomeClassToMock();
        SomeClassToMock spiedObject = SuperMock.spy(realObject);
        spiedObject.method();

        TestClass testInstance = new TestClass();
        SuperMock.initMocks(testInstance);
    }
}

interface SomeInterface {
    void doSmth();
}
