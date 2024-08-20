package logic.function.parser;

import component.api.CellType;
import logic.function.Function;
import logic.function.math.Abs;
import logic.function.math.Minus;
import logic.function.math.Plus;
import logic.function.returnable.MyBoolean;
import logic.function.returnable.MyNumber;
import logic.function.returnable.MyString;
import logic.function.string.Concat;
import logic.function.string.Sub;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public enum FunctionParser {
    IDENTITY {
        @Override
        public Function parse(List<java.lang.String> arguments) {
            if (arguments.size() != 1) {
                throw new IllegalArgumentException("Invalid number of arguments for IDENTITY function. Expected 1, but got " + arguments.size());
            }

            // create the relevant function instance
            java.lang.String actualValue = arguments.get(0).trim();
            if (isBoolean(actualValue)) {
                return new MyBoolean(java.lang.Boolean.parseBoolean(actualValue));
            } else if (isNumeric(actualValue)) {
                return new MyNumber(java.lang.Double.parseDouble(actualValue));
            } else {
                return new MyString(actualValue);
            }
        }

        private boolean isBoolean(java.lang.String value) {
            return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
        }

        private boolean isNumeric(java.lang.String value) {
            try {
                Double.parseDouble(value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    },

    PLUS {
        @Override
        public Function parse(List<String> arguments) {
            // validations of the function (e.g. number of arguments)
            if (arguments.size() != 2) {
                throw new IllegalArgumentException("Invalid number of arguments for PLUS function. Expected 2, but got " + arguments.size());
            }

            // structure is good. parse arguments
            Function firstArgument = parseFunction(arguments.get(0).trim());
            Function secondArgument = parseFunction(arguments.get(1).trim());

            if (!firstArgument.returnType().equals(CellType.NUMERIC) ||
                    !secondArgument.returnType().equals(CellType.NUMERIC)) {
                throw new IllegalArgumentException("Invalid argument types for PLUS function." +
                        " Expected NUMERIC, but got " + firstArgument.getFunctionName() +
                        " and " + secondArgument.getFunctionName());
            }

            return new Plus(firstArgument, secondArgument);
        }
    },

    MINUS {
        @Override
        public Function parse(List<String> arguments) {
            // validations of the function (e.g. number of arguments)
            if (arguments.size() != 2) {
                throw new IllegalArgumentException("Invalid number of arguments for PLUS function. Expected 2, but got " + arguments.size());
            }

            // structure is good. parse arguments
            Function firstArgument = parseFunction(arguments.get(0).trim());
            Function secondArgument = parseFunction(arguments.get(1).trim());

            if (!firstArgument.returnType().equals(CellType.NUMERIC) ||
                    !secondArgument.returnType().equals(CellType.NUMERIC)) {
                throw new IllegalArgumentException("Invalid argument types for MINUS function." +
                        " Expected NUMERIC, but got " + firstArgument.getFunctionName() +
                        " and " + secondArgument.getFunctionName());
            }

            return new Minus(firstArgument, secondArgument);
        }
    },

    CONCAT {
        @Override
        public Function parse(List<String> arguments) {
            // validations of the function (e.g. number of arguments)
            if (arguments.size() != 2) {
                throw new IllegalArgumentException("Invalid number of arguments for PLUS function. Expected 2, but got " + arguments.size());
            }

            // structure is good. parse arguments
            Function firstArgument = parseFunction(arguments.get(0).trim());
            Function secondArgument = parseFunction(arguments.get(1).trim());

            if (!firstArgument.returnType().equals(CellType.STRING) ||
                    !secondArgument.returnType().equals(CellType.STRING)) {
                throw new IllegalArgumentException("Invalid argument types for CONCAT function." +
                        " Expected STRING, but got " + firstArgument.getFunctionName() +
                        " and " + secondArgument.getFunctionName());
            }

            return new Concat(firstArgument, secondArgument);
        }
    },

    SUB {
        @Override
        public Function parse(List<String> arguments) {
            // validations of the function (e.g. number of arguments)
            if (arguments.size() != 3) {
                throw new IllegalArgumentException("Invalid number of arguments for PLUS function. Expected 2, but got " + arguments.size());
            }

            // structure is good. parse arguments
            Function firstArgument = parseFunction(arguments.get(0).trim());
            Function secondArgument = parseFunction(arguments.get(1).trim());
            Function thirdArgument = parseFunction(arguments.get(2).trim());

            if (!firstArgument.returnType().equals(CellType.STRING) ||
                    !secondArgument.returnType().equals(CellType.NUMERIC) ||
                    !thirdArgument.returnType().equals(CellType.NUMERIC)){
                throw new IllegalArgumentException("Invalid argument types for CONCAT function." +
                        " Expected STRING but got " + firstArgument.getFunctionName() +
                        "expected NUMERIC but got" + secondArgument.getFunctionName() + "and" + thirdArgument.getFunctionName());
            }

                return new Sub(firstArgument, secondArgument, thirdArgument);
        }
    },

    ABS {
        @Override
        public Function parse(List<String> arguments) {
            // validations of the function (e.g. number of arguments)
            if (arguments.size() != 1) {
                throw new IllegalArgumentException("Invalid number of arguments for PLUS function. Expected 2, but got " + arguments.size());
            }

            // structure is good. parse arguments
            Function firstArgument = parseFunction(arguments.get(0).trim());

            if (!firstArgument.returnType().equals(CellType.NUMERIC){

                throw new IllegalArgumentException("Invalid argument type for ABS function." +
                        " Expected NUMERIC but got " + firstArgument.getFunctionName();
            }

            return new Abs(firstArgument);
        }
    },

    REF{
        @Override
        public Function parse(List<String> arguments) {
            if (arguments.size() != 1) {
                throw new IllegalArgumentException("Invalid number of arguments for PLUS function. Expected 2, but got " + arguments.size());
            }

            Function firstArgument = parseFunction(arguments.get(0).trim());

            if (!firstArgument.returnType().equals(CellType.STRING)){
                throw new IllegalArgumentException("Invalid argument type for ABS function." +
                        " Expected NUMERIC but got " + firstArgument.getFunctionName();
            }

        }
    }


    public static Function parseFunction(String input){
        if(input.startsWith("{") && input.endsWith("}")){
            java.lang.String functionNameAndContent = input.substring(1, input.length()-1);
            List<java.lang.String> topLevelParts = parseMainParts(functionNameAndContent);
            java.lang.String functionName = topLevelParts.get(0).toUpperCase();
            topLevelParts.remove(0);
            return FunctionParser.valueOf(functionName).parse(topLevelParts);
        }
        else {
            return FunctionParser.IDENTITY.parse(List.of(input.trim()));
        }
    }

    private static List<String> parseMainParts(java.lang.String input) {
        List<java.lang.String> parts = new ArrayList<>();
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
                parts.add(buffer.toString().trim());
                buffer.setLength(0); // Clear the buffer for the next part
            } else {
                buffer.append(c);
            }
        }

        // Add the last part
        if (buffer.length() > 0) {
            parts.add(buffer.toString().trim());
        }

        return parts;
    }

    public abstract Function parse(List<String> parts);
}

