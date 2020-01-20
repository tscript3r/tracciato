package pl.tscript3r.tracciato;

import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;

/**
 * Works only for test method names
 * <p>
 * Test method name pattern:
 * testedMethod_Should_ExpectedBehavior_When_GivenState
 * <p>
 * Test method name example:
 * isNew_Should_ReturnTrue_When_IdIsNotSet
 * <p>
 * Output example:
 * isNew: Should return true when id is not set
 */
public class ReplaceCamelCaseAndUnderscores extends DisplayNameGenerator.Standard {

    private static final String UNDERSCORE = "_";

    public ReplaceCamelCaseAndUnderscores() {
    }

    public String generateDisplayNameForClass(Class<?> testClass) {
        return super.generateDisplayNameForClass(testClass);
    }

    public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
        return super.generateDisplayNameForNestedClass(nestedClass);
    }

    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
        return this.parse(testMethod.getName());
    }

    private String parse(String input) {
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append(copyUntilUnderscore(input));
        resultBuilder.append(": Should");
        input = deleteUntilUnderscore(deleteUntilUnderscore(input));
        resultBuilder.append(
                copyUntilUnderscore(addSpaces(input))
        );
        resultBuilder.append(" when");
        input = deleteUntilUnderscore(deleteUntilUnderscore(input));
        resultBuilder.append(addSpaces(input));
        return resultBuilder.toString();
    }

    private String copyUntilUnderscore(String input) {
        return input.substring(0, input.indexOf(UNDERSCORE));
    }

    private String deleteUntilUnderscore(String input) {
        if (input.contains(UNDERSCORE))
            return input.substring(input.indexOf(UNDERSCORE) + 1);
        else
            throw new IllegalArgumentException("Invalid test method name. Refactor to following pattern: " +
                    "testedMethod_Should_ExpectedBehavior_When_GivenState");
    }

    private String addSpaces(String input) {
        return addSpacesBeforeNumbers(replaceUpperCasesToSpaces(input));
    }

    private String addSpacesBeforeNumbers(String input) {
        if (input.matches(".*\\d.*")) { // containing any digits
            StringBuilder resultBuilder = new StringBuilder();
            for (int i = 0; i < input.length(); i++) {
                if (i > 0 && Character.isDigit(input.charAt(i)) && !Character.isDigit(input.charAt(i - 1)))
                    resultBuilder.append(" ");
                resultBuilder.append(input.charAt(i));
            }
            return resultBuilder.toString();
        } else
            return input;
    }

    private String replaceUpperCasesToSpaces(String input) {
        return input.replaceAll("([A-Z])", " $1").toLowerCase();
    }

}