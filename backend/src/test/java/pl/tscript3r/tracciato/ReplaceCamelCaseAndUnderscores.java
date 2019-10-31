package pl.tscript3r.tracciato;

import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;

/**
 * Works only for test method names
 *
 * Test method name pattern:
 * testedMethod_Should_ExpectedBehavior_When_GivenState
 *
 * Test method name example:
 * isNew_Should_ReturnTrue_When_IdIsNotSet
 *
 * Output example:
 * isNew: Should return true when id is not set
 */
public class ReplaceCamelCaseAndUnderscores extends DisplayNameGenerator.Standard {

    private static final String UNDERSCORE = "_";

    public ReplaceCamelCaseAndUnderscores() {
    }

    public String generateDisplayNameForClass(Class<?> testClass) {
        return this.replaceCapitals(super.generateDisplayNameForClass(testClass));
    }

    public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
        return this.replaceCapitals(super.generateDisplayNameForNestedClass(nestedClass));
    }

    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
        return this.replaceCapitals(testMethod.getName());
    }

    private String replaceCapitals(String input) {
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append(extractTillUnderscore(input));
        resultBuilder.append(": Should");
        input = cutToNextUnderscore(cutToNextUnderscore(input));
        resultBuilder.append(
                extractTillUnderscore(replaceUpperCasesToSpaces(input))
        );
        resultBuilder.append(" when");
        input = cutToNextUnderscore(cutToNextUnderscore(input));
        resultBuilder.append(replaceUpperCasesToSpaces(input));
        return resultBuilder.toString();
    }

    private String extractTillUnderscore(String input) {
        return input.substring(0, input.indexOf(UNDERSCORE));
    }

    private String cutToNextUnderscore(String input) {
        if(input.contains(UNDERSCORE))
            return input.substring(input.indexOf(UNDERSCORE) + 1);
        else
            throw new IllegalArgumentException("Invalid test method name. Refactor to following pattern: " +
                    "testedMethod_Should_ExpectedBehavior_When_GivenState");
    }

    private String replaceUpperCasesToSpaces(String input) {
        return input.replaceAll("([A-Z])", " $1").toLowerCase();
    }

}