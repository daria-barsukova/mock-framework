package mock;

import mock.annotations.Spy;
import mock.core.SuperMock;
import mock.matchers.ArgumentMatchers;
import org.junit.jupiter.api.Test;

import static mock.core.SuperMock.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SpyClassTest {
    @Spy
    CalculatorImpl firstCalculator = new CalculatorImpl();

    CalculatorImpl secondCalculator = SuperMock.spy(new CalculatorImpl());

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

        when(firstCalculator.divide(ArgumentMatchers.eq(4), ArgumentMatchers.eq(2)))
                .thenThrow(new ArithmeticException("Cannot perform operation"));
        when(secondCalculator.divide(ArgumentMatchers.eq(8), ArgumentMatchers.eq(2)))
                .thenThrow(new RuntimeException("Cannot perform operation"));

        // Бросаем замоканный Exception
        assertThrows(ArithmeticException.class, () -> firstCalculator.divide(4, 2));
        assertThrows(RuntimeException.class, () -> secondCalculator.divide(8, 2));

        // Вычисляем на основе дефолтной реализации, так как мок не задан
        assertEquals(1, firstCalculator.divide(2, 2));
        assertEquals(2, secondCalculator.divide(4, 2));
    }
}
