package de.tuda.prg.parser.visitorsje.sgxChecker.tud.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FieldDetector {

    private final Pattern variablePattern = Pattern.compile("[a-zA-Z][a-zA-Z0-9_.]*");
    private final Pattern methodPattern = Pattern.compile("[a-zA-Z][a-zA-Z0-9_.]*[\\([a-zA-Z][a-zA-Z0-9_.,]*\\)]");

    /**
     * This method extracts a list of matches for a passed challenge string.
     * The challenge string is an expression which will be matched with
     * the rules for a valid Java-field reference/declaration. Any reference will be
     * collected by this method.
     * @param challenge Expression to be matched against Java-rules
     * @return Array of matched substrings
     */
    public List<String> getMatches(String challenge) {
        Matcher variableMatcher = variablePattern.matcher(challenge);
        ArrayList<String> matches = new ArrayList<>();
        while (variableMatcher.find()) {
            matches.add(variableMatcher.group());
        }
        return this.removeMethodCalls(challenge, matches);
    }

    /**
     * This method filters all strings matching Java method-calls.
     * @param challenge String to be matched against Java rules
     * @param variableMatches Array of matches detected by getMatches
     * @return Array of filtered matched substrings.
     */
    private List<String> removeMethodCalls(String challenge, ArrayList<String> variableMatches) {
        Matcher methodMatcher = methodPattern.matcher(challenge);
        ArrayList<String> matches = new ArrayList<>();
        while (methodMatcher.find()) {
            String match = methodMatcher.group();
            // Remove the last element of the string which is always the bracket
            // has to be done to be able to compare it with the found variable matches
            String matchWithoutParanthese = match.substring(0, match.length() - 1);
            matches.add(matchWithoutParanthese);
        }

        return variableMatches.stream().
                filter(match -> !matches.contains(match))
                .collect(Collectors.toList());
    }
}
