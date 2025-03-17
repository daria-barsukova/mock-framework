package mock;

import mock.core.SuperMock;

public class Main {
    public static void main(String[] args) {

        SomeClassToMock mockedClass = SuperMock.mock(SomeClassToMock.class);
        SuperMock.when(mockedClass.someMethod("string")).thenReturn("Mocked Result");
        System.out.println(mockedClass.someMethod("string"));

        SomeClassToMock anotherMockedClass = SuperMock.mock(SomeClassToMock.class);
        SuperMock.when(anotherMockedClass.methodReturningValue()).thenThrow(new RuntimeException("Exception from mock"));

        try {
            anotherMockedClass.methodReturningValue();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println(mockedClass.someMethod("string"));
    }
}

interface SomeInterface {
    void doSmth();
}
