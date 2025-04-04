package mock;

import mock.annotations.Mock;
import mock.core.SuperMock;
import mock.matchers.ArgumentMatchers;
import org.junit.jupiter.api.Test;

import static mock.core.SuperMock.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MockClassTest {
    @Mock
    CalculatorImpl firstCalculator;

    CalculatorImpl secondCalculator = SuperMock.mock(CalculatorImpl.class);

    @Test
    void testThenReturn() {
        SuperMock.initMocks(this);

        when(firstCalculator.add(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
                .thenReturn(10);
        when(secondCalculator.add(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
                .thenReturn(20);

        // Возвращаем замоканное значение
        assertEquals(10, firstCalculator.add(2, 3));
        assertEquals(20, secondCalculator.add(2, 3));
    }

    @Test
    void testThenThrow() {
        SuperMock.initMocks(this);

        when(firstCalculator.divide(ArgumentMatchers.eq(2), ArgumentMatchers.eq(0)))
                .thenThrow(new ArithmeticException("Cannot divide by zero"));
        when(secondCalculator.divide(ArgumentMatchers.eq(5), ArgumentMatchers.eq(0)))
                .thenThrow(new RuntimeException("Cannot perform operation"));

        // Бросаем замоканный Exception
        assertThrows(ArithmeticException.class, () -> firstCalculator.divide(2, 0));
        assertThrows(RuntimeException.class, () -> secondCalculator.divide(5, 0));

        // Возвращаем дефолтное значение, так как мок не задан
        assertEquals(0, firstCalculator.divide(2, 2));
        assertEquals(0, secondCalculator.divide(4, 2));
    }
}
