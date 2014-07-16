package net.orfjackal.experimental;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Esko Luontola
 * @since 17.7.2014
 */
@RunWith(Parameterized.class)
public class Java8FunctionalInterfaceNamingTest {

    private final String expectedMethodName;
    private final String expectedClassName;
    private final Java8FunctionalInterfaceNaming generator;

    public Java8FunctionalInterfaceNamingTest(String args, String returns, String methodName, String className) {
        this.expectedMethodName = methodName;
        this.expectedClassName = className;
        this.generator = new Java8FunctionalInterfaceNaming(args, returns);
    }

    @Parameterized.Parameters(name = "({0}) -> {1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"double", "", "accept", "DoubleConsumer"},
                {"int", "", "accept", "IntConsumer"},
                {"long", "", "accept", "LongConsumer"},
                {"T", "", "accept", "Consumer"},
                {"T, double", "", "accept", "ObjDoubleConsumer"},
                {"T, int", "", "accept", "ObjIntConsumer"},
                {"T, long", "", "accept", "ObjLongConsumer"},
                {"T, U", "", "accept", "BiConsumer"},
                {"double", "double", "applyAsDouble", "DoubleUnaryOperator"},
                {"int", "double", "applyAsDouble", "IntToDoubleFunction"},
                {"long", "double", "applyAsDouble", "LongToDoubleFunction"},
                {"T", "double", "applyAsDouble", "ToDoubleFunction"},
                {"double", "int", "applyAsInt", "DoubleToIntFunction"},
                {"int", "int", "applyAsInt", "IntUnaryOperator"},
                {"long", "int", "applyAsInt", "LongToIntFunction"},
                {"T", "int", "applyAsInt", "ToIntFunction"},
                {"double", "long", "applyAsLong", "DoubleToLongFunction"},
                {"int", "long", "applyAsLong", "IntToLongFunction"},
                {"long", "long", "applyAsLong", "LongUnaryOperator"},
                {"T", "long", "applyAsLong", "ToLongFunction"},
                {"double", "R", "apply", "DoubleFunction"},
                {"int", "R", "apply", "IntFunction"},
                {"long", "R", "apply", "LongFunction"},
                {"T", "R", "apply", "Function"},
                {"T", "T", "apply", "UnaryOperator"},
                {"double, double", "double", "applyAsDouble", "DoubleBinaryOperator"},
                {"T, U", "double", "applyAsDouble", "ToDoubleBiFunction"},
                {"int, int", "int", "applyAsInt", "IntBinaryOperator"},
                {"T, U", "int", "applyAsInt", "ToIntBiFunction"},
                {"long, long", "long", "applyAsLong", "LongBinaryOperator"},
                {"T, U", "long", "applyAsLong", "ToLongBiFunction"},
                {"T, U", "R", "apply", "BiFunction"},
                {"T, T", "T", "apply", "BinaryOperator"},
                {"", "T", "get", "Supplier"},
                {"", "boolean", "getAsBoolean", "BooleanSupplier"},
                {"", "double", "getAsDouble", "DoubleSupplier"},
                {"", "int", "getAsInt", "IntSupplier"},
                {"", "long", "getAsLong", "LongSupplier"},
                {"double", "boolean", "test", "DoublePredicate"},
                {"int", "boolean", "test", "IntPredicate"},
                {"long", "boolean", "test", "LongPredicate"},
                {"T", "boolean", "test", "Predicate"},
                {"T, U", "boolean", "test", "BiPredicate"}
        });
    }

    @Test
    public void generates_method_names() {
        assertThat(generator.getMethodName(), is(expectedMethodName));
    }

    @Test
    public void generates_class_names() {
        assertThat(generator.getClassName(), is(expectedClassName));
    }

    @Test
    public void examples_match_the_Java_library() throws Exception {
        Class<?> clazz = Class.forName("java.util.function." + expectedClassName);
        Method method = clazz.getMethod(expectedMethodName, generator.getParameterTypes());
        assertThat("return type", method.getReturnType(), is(equalTo(generator.getReturnType())));
    }
}
