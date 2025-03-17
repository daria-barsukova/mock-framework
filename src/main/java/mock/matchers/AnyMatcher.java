package mock.matchers;

public class AnyMatcher<T> implements Matcher<T> {
    @Override
    public boolean matches(T value) {
        return true;
    }
}
