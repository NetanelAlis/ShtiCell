package logic.parser;

import component.api.CellType;
import component.sheet.api.Sheet;
import logic.function.Function;
import logic.function.math.*;
import logic.function.returnable.SpecialValue;
import logic.function.string.Concat;
import logic.function.string.Sub;
import logic.function.system.Identity;
import logic.function.system.Ref;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public enum FunctionParser {
        IDENTITY{
        @Override
        public Function parse(List<String> arguments) {
            if (arguments.size() != 1) {
                throw new IllegalArgumentException("Invalid number of arguments for IDENTITY function." +
                        " Expected 1, but got " + arguments.size());
            }

            String value = arguments.getFirst();
            if (value.isEmpty()) {
                return new Identity(value, CellType.NO_VALUE);
            } else if (isBoolean(value)) {
                return new Identity(Boolean.parseBoolean(value), CellType.BOOLEAN);
            } else if (isNumber(value)) {
                return new Identity(Double.parseDouble(value), CellType.NUMERIC);
            } else {
                return new Identity(value, CellType.STRING);
            }
        }

        private boolean isBoolean(String value) {
            return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
        }

        private boolean isNumber(String value) {
            try {
                Double.parseDouble(value);
                return value.equals(value.trim());
            } catch (NumberFormatException e) {
                return false;
            }
        }
    },
    ABS{
        @Override
        public Function parse(List<String> arguments) {
            if (arguments.size() != 1) {
                throw new IllegalArgumentException("Invalid number of arguments for ABS function." +
                        " Expected 1, but got " + arguments.size());
            }

            Function argument = parseFunction(arguments.getFirst());

            CellType type = argument.getReturnType();

            if (!type.equals(CellType.NUMERIC) && !type.equals(CellType.UNKNOWN)) {
                throw new IllegalArgumentException("Invalid argument types for ABS function." +
                        " Expected NUMERIC, but got " + argument.getReturnType());
            }

            return new Abs(argument);
        }
    },
    DIVIDE{
        @Override
        public Function parse(List<String> arguments) {
            if (arguments.size() != 2) {
                throw new IllegalArgumentException("Invalid number of arguments for DIVIDE function." +
                        " Expected 2, but got " + arguments.size());
            }

            Function firstArgument = parseFunction(arguments.getFirst());
            Function secondArgument = parseFunction(arguments.getLast());

            CellType firstArgType = firstArgument.getReturnType();
            CellType secondArgType = secondArgument.getReturnType();

            if ((!firstArgType.equals(CellType.NUMERIC) && !firstArgType.equals(CellType.UNKNOWN)) ||
                    (!secondArgType.equals(CellType.NUMERIC) && !secondArgType.equals(CellType.UNKNOWN))) {
                throw new IllegalArgumentException("Invalid argument types for DIVIDE function." +
                        " Expected NUMERIC, but got " + firstArgument.getReturnType() +
                        " and " + secondArgument.getReturnType());
            }

            return new Divide(firstArgument, secondArgument);
        }
    },
    MINUS{
        @Override
        public Function parse(List<String> arguments) {
            if (arguments.size() != 2) {
                throw new IllegalArgumentException("Invalid number of arguments for MINUS function." +
                        " Expected 2, but got " + arguments.size());
            }

            Function firstArgument = parseFunction(arguments.getFirst());
            Function secondArgument = parseFunction(arguments.getLast());

            CellType firstArgType = firstArgument.getReturnType();
            CellType secondArgType = secondArgument.getReturnType();

            if ((!firstArgType.equals(CellType.NUMERIC) && !firstArgType.equals(CellType.UNKNOWN)) ||
                    (!secondArgType.equals(CellType.NUMERIC) && !secondArgType.equals(CellType.UNKNOWN))) {
                throw new IllegalArgumentException("Invalid argument types for MINUS function." +
                        " Expected NUMERIC, but got " + firstArgument.getReturnType() +
                        " and " + secondArgument.getReturnType());
            }

            return new Minus(firstArgument, secondArgument);
        }
    },
    MOD{
        @Override
        public Function parse(List<String> arguments) {
            if (arguments.size() != 2) {
                throw new IllegalArgumentException("Invalid number of arguments for MOD function." +
                        " Expected 2, but got " + arguments.size());
            }

            Function firstArgument = parseFunction(arguments.getFirst());
            Function secondArgument = parseFunction(arguments.getLast());

            CellType firstArgType = firstArgument.getReturnType();
            CellType secondArgType = secondArgument.getReturnType();

            if ((!firstArgType.equals(CellType.NUMERIC) && !firstArgType.equals(CellType.UNKNOWN)) ||
                    (!secondArgType.equals(CellType.NUMERIC) && !secondArgType.equals(CellType.UNKNOWN))) {
                throw new IllegalArgumentException("Invalid argument types for MOD function." +
                        " Expected NUMERIC, but got " + firstArgument.getReturnType() +
                        " and " + secondArgument.getReturnType());
            }

            return new Mod(firstArgument, secondArgument);
        }
    },
    PLUS{
        @Override
        public Function parse(List<String> arguments) {
            if (arguments.size() != 2) {
                throw new IllegalArgumentException("Invalid number of arguments for PLUS function." +
                        " Expected 2, but got " + arguments.size());
            }

            Function firstArgument = parseFunction(arguments.getFirst());
            Function secondArgument = parseFunction(arguments.getLast());

            CellType firstArgType = firstArgument.getReturnType();
            CellType secondArgType = secondArgument.getReturnType();

            if ((!firstArgType.equals(CellType.NUMERIC) && !firstArgType.equals(CellType.UNKNOWN)) ||
                    (!secondArgType.equals(CellType.NUMERIC) && !secondArgType.equals(CellType.UNKNOWN))) {
                throw new IllegalArgumentException("Invalid argument types for PLUS function." +
                        " Expected NUMERIC, but got " + firstArgument.getReturnType() +
                        " and " + secondArgument.getReturnType());
            }

            return new Plus(firstArgument, secondArgument);
        }
    },
    POW{
        @Override
        public Function parse(List<String> arguments) {
            if (arguments.size() != 2) {
                throw new IllegalArgumentException("Invalid number of arguments for POW function." +
                        " Expected 2, but got " + arguments.size());
            }

            Function firstArgument = parseFunction(arguments.getFirst());
            Function secondArgument = parseFunction(arguments.getLast());

            CellType firstArgType = firstArgument.getReturnType();
            CellType secondArgType = secondArgument.getReturnType();

            if ((!firstArgType.equals(CellType.NUMERIC) && !firstArgType.equals(CellType.UNKNOWN)) ||
                    (!secondArgType.equals(CellType.NUMERIC) && !secondArgType.equals(CellType.UNKNOWN))) {
                throw new IllegalArgumentException("Invalid argument types for POW function." +
                        " Expected NUMERIC, but got " + firstArgument.getReturnType() +
                        " and " + secondArgument.getReturnType());
            }

            return new Pow(firstArgument, secondArgument);
        }
    },
    TIMES{
        @Override
        public Function parse(List<String> arguments) {
            if (arguments.size() != 2) {
                throw new IllegalArgumentException("Invalid number of arguments for TIMES function." +
                        " Expected 2, but got " + arguments.size());
            }

            Function firstArgument = parseFunction(arguments.getFirst());
            Function secondArgument = parseFunction(arguments.getLast());

            CellType firstArgType = firstArgument.getReturnType();
            CellType secondArgType = secondArgument.getReturnType();

            if ((!firstArgType.equals(CellType.NUMERIC) && !firstArgType.equals(CellType.UNKNOWN)) ||
                    (!secondArgType.equals(CellType.NUMERIC) && !secondArgType.equals(CellType.UNKNOWN))) {
                throw new IllegalArgumentException("Invalid argument types for TIMES function." +
                        " Expected NUMERIC, but got " + firstArgument.getReturnType() +
                        " and " + secondArgument.getReturnType());
            }

            return new Times(firstArgument, secondArgument);
        }
    },
    CONCAT{
        @Override
        public Function parse(List<String> arguments) {
            if (arguments.size() != 2) {
                throw new IllegalArgumentException("Invalid number of arguments for CONCAT function." +
                        " Expected 2, but got " + arguments.size());
            }

            Function firstArgument = parseFunction(arguments.getFirst());
            Function secondArgument = parseFunction(arguments.getLast());

            CellType firstArgType = firstArgument.getReturnType();
            CellType secondArgType = secondArgument.getReturnType();

            if ((!firstArgType.equals(CellType.STRING) && !firstArgType.equals(CellType.UNKNOWN)) ||
                    (!secondArgType.equals(CellType.STRING) && !secondArgType.equals(CellType.UNKNOWN))) {
                throw new IllegalArgumentException("Invalid argument types for CONCAT function." +
                        " Expected STRING, but got " + firstArgument.getReturnType() +
                        " and " + secondArgument.getReturnType());
            }

            return new Concat(firstArgument, secondArgument);
        }
    },
    SUB{
        @Override
        public Function parse(List<String> arguments) {
            if (arguments.size() != 3) {
                throw new IllegalArgumentException("Invalid number of arguments for SUB function." +
                        " Expected 3, but got " + arguments.size());
            }

            Function firstArgument = parseFunction(arguments.getFirst());
            Function secondArgument = parseFunction(arguments.get(1));
            Function thirdArgument = parseFunction(arguments.getLast());

            CellType firstArgType = firstArgument.getReturnType();
            CellType secondArgType = secondArgument.getReturnType();
            CellType thirdArgType = thirdArgument.getReturnType();

            if ((!firstArgType.equals(CellType.STRING) && !firstArgType.equals(CellType.UNKNOWN)) ||
                    (!secondArgType.equals(CellType.NUMERIC) && !secondArgType.equals(CellType.UNKNOWN)) ||
                    (!thirdArgType.equals(CellType.NUMERIC) && !thirdArgType.equals(CellType.UNKNOWN))) {
                throw new IllegalArgumentException("Invalid argument types for SUB function." +
                        " Expected STRING, NUMERIC and NUMERIC, but got " + firstArgument.getReturnType() +
                        ", " + secondArgument.getReturnType() + " and " + thirdArgument.getReturnType());
            }

            return new Sub(firstArgument, secondArgument, thirdArgument);
        }
    },
    REF{
        @Override
        public Function parse(List<String> arguments) {
            if (arguments.size() != 1) {
                throw new IllegalArgumentException("Invalid number of arguments for REF function." +
                        " Expected 1, but got " + arguments.size());
            }

            String target = arguments.getFirst();
            if (!Sheet.isValidCellID(target)) {
                throw new IllegalArgumentException("Invalid argument for REF function." +
                        " Expected a valid cell ID, but got " + target);
            }

            return new Ref(target);
        }
    };

    abstract public Function parse(List<String> arguments);

    public static Function parseFunction(String input){
        if(input.startsWith("{") && input.endsWith("}")){

            String functionContent = input.substring(1, input.length() - 1);
            List<String> topLevelParts = parseMainParts(functionContent);

            String functionName = topLevelParts.getFirst().trim().toUpperCase();

            topLevelParts.removeFirst();
            try{
                return FunctionParser.valueOf(functionName).parse(topLevelParts);
            }
            catch(IllegalArgumentException e){
                throw new IllegalArgumentException("Invalid function name: " + functionName);
            }
        }

        return FunctionParser.IDENTITY.parse(List.of(input));
    }

    private static List<String> parseMainParts(String input) {
        List<String> parts = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (char c : input.toCharArray()) {
            if (c == '{') {
                stack.push(c);
            } else if (c == '}') {
                stack.pop();
            }

            if (c == ',' && stack.isEmpty()) {
                // If we are at a comma and the stack is empty, it's a separator for top-level parts
                parts.add(buffer.toString());
                buffer.setLength(0); // Clear the buffer for the next part
            } else {
                buffer.append(c);
            }
        }

        // Add the last part
        if (!buffer.isEmpty()) {
            parts.add(buffer.toString());
        }

        return parts;
    }
}
