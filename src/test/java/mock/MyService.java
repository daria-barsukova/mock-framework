package mock;

public class MyService {
    public String process(String input) {
        return "Processed: " + input;
    }

    public int calculate(int a, int b) {
        return a + b;
    }

    public boolean checkCondition(String value) {
        return value.equals("valid");
    }
}
