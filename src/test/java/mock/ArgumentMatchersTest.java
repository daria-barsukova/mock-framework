package mock;

import mock.core.SuperMock;
import mock.matchers.ArgumentMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static mock.core.SuperMock.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArgumentMatchersTest {
    private MyService mockService;

    @BeforeEach
    void setUp() {
        mockService = SuperMock.mock(MyService.class);
    }

    @Test
    void testEqMatcher() {
        when(mockService.process(ArgumentMatchers.eq("hello"))).thenReturn("mocked response");

        assertEquals("mocked response", mockService.process("hello"));
        assertNull(mockService.process("world")); // Для другого значения мок не задан
    }

    @Test
    void testAnyMatcher() {
        when(mockService.process(ArgumentMatchers.any())).thenReturn("mocked for any");

        assertEquals("mocked for any", mockService.process("random"));
        assertEquals("mocked for any", mockService.process(null));
    }

    @Test
    void testAnyIntMatcher() {
        when(mockService.calculate(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(42);

        assertEquals(42, mockService.calculate(1, 2));
        assertEquals(42, mockService.calculate(-10, 999));
    }

    @Test
    void testMatchesRegexMatcher() {
        when(mockService.process(ArgumentMatchers.matchesRegex("\\d+"))).thenReturn("mocked for numbers");

        assertEquals("mocked for numbers", mockService.process("12345"));
        assertNull(mockService.process("abc")); // Не соответствует регулярному выражению
    }

    @Test
    void testMatchesPredicateMatcher() {
        when(mockService.checkCondition(ArgumentMatchers.matchesPredicate(s -> s.length() > 3)))
                .thenReturn(true);

        assertTrue(mockService.checkCondition("longValue"));
        assertFalse(mockService.checkCondition("no")); // Длина < 3, не попадает под предикат
    }
}