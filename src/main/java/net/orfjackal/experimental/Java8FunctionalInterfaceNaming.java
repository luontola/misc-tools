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
        if (args.length == 0) {
            return primitiveName(returns) + "Supplier";
        }

        if (returns(void.class)) {
            if (args.length == 1) {
                return primitiveName(args[0]) + "Consumer";
            }
            if (args.length == 2) {
                if (isGeneric(args[0]) && isGeneric(args[1])) {
                    return "BiConsumer";
                }
                if (isGeneric(args[0])) {
                    return "Obj" + capitalize(args[1]) + "Consumer";
                }
                throw new AssertionError();
            }
        }

        if (returns(boolean.class)) {
            if (args.length == 1) {
                return primitiveName(args[0]) + "Predicate";
            }
            if (args.length == 2) {
                return "BiPredicate";
            }
        }

        if (allEqual(args, returns)) {
            if (args.length == 1) {
                return primitiveName(args[0]) + "UnaryOperator";
            }
            if (args.length == 2) {
                return primitiveName(args[0]) + "BinaryOperator";
            }
        }

        if (args.length == 1) {
            if (isGeneric(args[0]) && isGeneric(returns)) {
                return "Function";
            }
            if (isGeneric(args[0])) {
                return "To" + capitalize(returns) + "Function";
            }
            if (isGeneric(returns)) {
                return capitalize(args[0]) + "Function";
            }
            return capitalize(args[0]) + "To" + capitalize(returns) + "Function";
        }
        if (args.length == 2) {
            if (isGeneric(returns)) {
                return "BiFunction";
            }
            return "To" + capitalize(returns) + "BiFunction";
        }
        return null;
    }

    public String getMethodName() {
        if (args.length == 0) {
            if (isGeneric(returns)) {
                return "get";
            } else {
                return "getAs" + capitalize(returns);
            }
        }

        if (returns(void.class)) {
            return "accept";
        }

        if (returns(boolean.class)) {
            return "test";
        }

        if (isGeneric(returns)) {
            return "apply";
        } else {
            return "applyAs" + capitalize(returns);
        }
    }

    private static String primitiveName(String type) {
        if (isPrimitive(type)) {
            return capitalize(type);
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
