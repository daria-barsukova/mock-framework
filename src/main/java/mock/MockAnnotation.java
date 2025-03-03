package mock;

import java.lang.reflect.Field;

public class MockAnnotation {
    public static void initMocks(Object instance) {
        Field[] fields = instance.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Mock.class)) {
                    field.set(instance, SuperMock.mock(field.getType()));
                } else if (field.isAnnotationPresent(Spy.class)) {
                    Object value = field.get(instance);
                    if (value == null) {
                        value = SuperMock.mock(field.getType());
                    }
                    field.set(instance, SuperMock.spy(value));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to init mock for field: " + field.getName(), e);
            }
        }
    }
}
