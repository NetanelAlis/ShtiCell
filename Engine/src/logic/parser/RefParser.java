package logic.parser;

import java.util.HashSet;
import java.util.Set;

public enum RefParser {

    PARSE;
    public Set<String> extractRefs(String input) {
        Set<String> refs = new HashSet<>();
        String target = "{REF,";
        int startIndex = 0;
        startIndex = input.indexOf(target, startIndex);

        while ((startIndex != -1)){
            int refStart = startIndex + target.length();
            int refEnd = input.indexOf('}', refStart);
            if (refEnd != -1) {
                String ref = input.substring(refStart, refEnd);
                refs.add(ref);
                startIndex = refEnd + 1; // Move past the current closing brace
            } else {
                break; // No closing brace found, exit loop
            }
            startIndex = input.indexOf(target, startIndex);
        }

        return refs;
    }
}
