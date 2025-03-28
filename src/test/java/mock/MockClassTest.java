package mock;

import mock.annotations.Mock;
import mock.core.SuperMock;
import mock.matchers.ArgumentMatchers;
import org.junit.jupiter.api.Test;

import static mock.core.SuperMock.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MockClassTest {
    @Mock
    CalculatorImpl calculator;

    @Test
    void testThenReturn() {
        SuperMock.initMocks(this);
        when(calculator.add(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
                .thenReturn(10);

        assertEquals(10, calculator.add(2, 3));
    }
}
