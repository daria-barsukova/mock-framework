package mock;

import mock.core.StaticStubber;
import mock.core.SuperMock;

public class Main {
    public static void main(String[] args) {

        SomeClassToMock mockedClass = SuperMock.mock(SomeClassToMock.class);
        SuperMock.when(mockedClass.someMethod("string")).thenReturn("Mocked Result");
        System.out.println(mockedClass.someMethod("string"));

        SuperMock.when(mockedClass.someMethod("string2")).thenReturn("Mocked Result2");
        System.out.println(mockedClass.someMethod("string2"));

        SomeClassToMock anotherMockedClass = SuperMock.mock(SomeClassToMock.class);
        SuperMock.when(anotherMockedClass.methodReturningValue()).thenThrow(new RuntimeException("Exception from mock"));

        try {
            anotherMockedClass.methodReturningValue();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println(mockedClass.someMethod("string"));

        System.out.println(mockedClass.someMethod("string2"));

        try (StaticStubber<SomeClassToMock> ignored = SuperMock.mockStatic(SomeClassToMock.class)) {
            SuperMock.when(SomeClassToMock.staticMethod("string")).thenReturn("Mocked Static Result");

            String result = SomeClassToMock.staticMethod("string");
            System.out.println(result);
        }

        String originalResult = SomeClassToMock.staticMethod("string");
        System.out.println(originalResult);
    }
}

interface SomeInterface {
    void doSmth();
}
