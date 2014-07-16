package net.orfjackal.experimental;

import java.util.Arrays;

/**
 * @author Esko Luontola
 * @since 17.7.2014
 */
public class Java8FunctionalInterfaceNaming {

    private final String[] args;
    private final String returns;

    public Java8FunctionalInterfaceNaming(String args, String returns) {
        this.returns = returns;
        this.args = Arrays.asList(args.split(", "))
                .stream()
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
    }

    public String getClassName() {

        // Suppliers
        if (args.length == 0) {
            return tagPrimitive(returns) + "Supplier";
        }

        // Consumers
        if (returns(void.class)) {
            if (args.length == 1) {
                return tagPrimitive(args[0]) + "Consumer";
            }
            if (args.length == 2) {
                if (isGeneric(args[0]) && isGeneric(args[1])) {
                    return "BiConsumer";
                }
                if (isGeneric(args[0])) {
                    return "Obj" + tagPrimitive(args[1]) + "Consumer";
                }
            }
        }

        // Predicates
        if (returns(boolean.class)) {
            if (args.length == 1) {
                return tagPrimitive(args[0]) + "Predicate";
            }
            if (args.length == 2) {
                return "BiPredicate";
            }
        }

        // Operators
        if (allEqual(args, returns)) {
            if (args.length == 1) {
                return tagPrimitive(args[0]) + "UnaryOperator";
            }
            if (args.length == 2) {
                return tagPrimitive(args[0]) + "BinaryOperator";
            }
        }

        // Functions
        if (args.length == 1) {
            return tagPrimitive(args[0]) + tagToPrimitive(returns) + "Function";
        }
        if (args.length == 2) {
            return tagToPrimitive(returns) + "BiFunction";
        }
        return null;
    }

    public String getMethodName() {

        // Suppliers
        if (args.length == 0) {
            return "get" + tagAsPrimitive(returns);
        }

        // Consumers
        if (returns(void.class)) {
            return "accept";
        }

        // Predicates
        if (returns(boolean.class)) {
            return "test";
        }

        // Functions & Operators
        return "apply" + tagAsPrimitive(returns);
    }

    private static String tagPrimitive(String type) {
        if (isPrimitive(type)) {
            return capitalize(type);
        } else {
            return "";
        }
    }

    private static String tagAsPrimitive(String type) {
        if (isPrimitive(type)) {
            return "As" + capitalize(type);
        } else {
            return "";
        }
    }

    private static String tagToPrimitive(String type) {
        if (isPrimitive(type)) {
            return "To" + capitalize(type);
        } else {
            return "";
        }
    }

    private static boolean isPrimitive(String type) {
        return !isGeneric(type);
    }

    private static boolean isGeneric(String type) {
        return type.length() == 1;
    }

    private static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private boolean returns(Class<?> type) {
        if (type == void.class) {
            return returns.equals("");
        } else {
            return returns.equals(type.getName());
        }
    }

    private static boolean allEqual(String[] args, String returns) {
        for (String arg : args) {
            if (!arg.equals(returns)) {
                return false;
            }
        }
        return true;
    }
}
