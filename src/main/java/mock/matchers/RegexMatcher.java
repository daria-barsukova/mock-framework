package mock.matchers;

public class RegexMatcher implements Matcher<String> {
    private final String regex;

    public RegexMatcher(String regex) {
        this.regex = regex;
    }

    @Override
    public boolean matches(String value) {
        return value != null && value.matches(regex);
    }
}
