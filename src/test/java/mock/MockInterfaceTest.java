package mock;

import mock.core.SuperMock;
import mock.matchers.ArgumentMatchers;
import org.junit.jupiter.api.Test;

import static mock.core.SuperMock.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MockInterfaceTest {
    Calculator calculator = SuperMock.mock(Calculator.class);

    @Test
    void testThenReturn() {
        when(calculator.add(ArgumentMatchers.eq(2), ArgumentMatchers.eq(3)))
                .thenReturn(10);

        // Возвращаем замоканное значение
        assertEquals(10, calculator.add(2, 3));
        assertEquals(0, calculator.add(5, 5));
    }

    @Test
    void testThenThrow() {
        when(calculator.divide(ArgumentMatchers.eq(10), ArgumentMatchers.eq(0)))
                .thenThrow(new ArithmeticException("Cannot divide by zero"));

        // Бросаем замоканный Exception
        assertThrows(ArithmeticException.class, () -> calculator.divide(10, 0));
    }
}
