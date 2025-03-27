package mock;
import mock.core.SuperMock;
import mock.matchers.ArgumentMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static mock.core.SuperMock.when;
import static org.junit.jupiter.api.Assertions.*;

public class StaticTest {

    @BeforeAll
    static void setup() {
        SuperMock.mockStatic(MathUtils.class);
    }


    @Test
    void testMockStaticMethodThenReturn() {
        when(MathUtils.add(ArgumentMatchers.eq(2), ArgumentMatchers.eq(3)))
                .thenReturn(10);

        assertEquals(10, MathUtils.add(2, 3));
        assertNotEquals(5, MathUtils.add(2, 3));
    }

    @Test
    void testMockStaticMethodWithDifferentArguments() {
        when(MathUtils.add(ArgumentMatchers.eq(4), ArgumentMatchers.eq(5)))
                .thenReturn(20);

        assertEquals(20, MathUtils.add(4, 5));
        assertEquals(0, MathUtils.add(10, 10));
    }

    @Test
    void testMockStaticMethodThenThrow() {

        SuperMock.when(MathUtils.divide(ArgumentMatchers.eq(10), ArgumentMatchers.eq(1)))
                .thenThrow(new ArithmeticException("Cannot divide by zero"));


        try {
            MathUtils.divide(10, 1);
        } catch (ArithmeticException e) {
            System.out.println("Caught exception: " + e.getMessage());
        }


        assertThrows(ArithmeticException.class, () -> MathUtils.divide(10, 1));


        SuperMock.when(MathUtils.divide(ArgumentMatchers.eq(10), ArgumentMatchers.eq(2)))
                .thenReturn(10);

        assertEquals(10, MathUtils.divide(10, 2));
    }
}
