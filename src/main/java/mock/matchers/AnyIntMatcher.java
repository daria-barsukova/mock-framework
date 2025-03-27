package mock.matchers;

public class AnyIntMatcher<T> implements Matcher<T> {
    @Override
    public boolean matches(T value) {
        return value instanceof Integer;
    }
}
