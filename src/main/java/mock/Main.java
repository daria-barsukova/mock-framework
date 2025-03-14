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

        SomeClassToMock mockedClass = SuperMock.mock(SomeClassToMock.class);
        SuperMock.when(mockedClass.methodReturningValue()).thenReturn("Mocked Result");
        System.out.println(mockedClass.methodReturningValue());

        SomeClassToMock anotherMockedClass = SuperMock.mock(SomeClassToMock.class);

        SuperMock.when(anotherMockedClass).thenThrow(new RuntimeException("Exception from mock"));

        try {
            anotherMockedClass.method();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        SomeClassToMock spyObject = SuperMock.spy(new SomeClassToMock());

        SuperMock.when(spyObject).invokeRealMethod();

        spyObject.method();
    }
}

interface SomeInterface {
    void doSmth();
}
