package mock;

import mock.core.StaticStubber;
import mock.core.SuperMock;
import mock.matchers.ArgumentMatchers;

public class Main {
    public static void main(String[] args) {

        SomeClassToMock mockedClass = SuperMock.mock(SomeClassToMock.class);
        SuperMock.when(mockedClass.someMethod(ArgumentMatchers.eq("string"))).thenReturn("Mocked Result");
        System.out.println(mockedClass.someMethod("string"));

        SuperMock.when(mockedClass.someMethod(ArgumentMatchers.any())).thenReturn("Mocked Result2");
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
            SuperMock.when(SomeClassToMock.staticMethod(ArgumentMatchers.eq("string"))).thenReturn("Mocked Static Result");

            String result = SomeClassToMock.staticMethod("string");
            System.out.println(result);
        }
        try (StaticStubber<SomeClassToMock> ignored = SuperMock.mockStatic(SomeClassToMock.class)) {
            SuperMock.when(SomeClassToMock.staticMethod(ArgumentMatchers.any()))
                    .thenThrow(new RuntimeException("Mocked exceptionyyyyy"));

            try {
                SomeClassToMock.staticMethod("string");
            } catch (Exception e) {
                System.out.println("Caught exception: " + e.getMessage());
            }

            System.out.println("Program continues execution...");
        }
        String originalResult = SomeClassToMock.staticMethod("string");
        System.out.println(originalResult);
    }
}
