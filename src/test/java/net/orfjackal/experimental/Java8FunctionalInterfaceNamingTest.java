package net.orfjackal.experimental;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.*;

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
                {"", "", "run", Runnable.class.getName()},
                {"double", "", "accept", DoubleConsumer.class.getName()},
                {"int", "", "accept", IntConsumer.class.getName()},
                {"long", "", "accept", LongConsumer.class.getName()},
                {"T", "", "accept", Consumer.class.getName()},
                {"T, double", "", "accept", ObjDoubleConsumer.class.getName()},
                {"T, int", "", "accept", ObjIntConsumer.class.getName()},
                {"T, long", "", "accept", ObjLongConsumer.class.getName()},
                {"T, U", "", "accept", BiConsumer.class.getName()},
                {"double", "double", "applyAsDouble", DoubleUnaryOperator.class.getName()},
                {"int", "double", "applyAsDouble", IntToDoubleFunction.class.getName()},
                {"long", "double", "applyAsDouble", LongToDoubleFunction.class.getName()},
                {"T", "double", "applyAsDouble", ToDoubleFunction.class.getName()},
                {"double", "int", "applyAsInt", DoubleToIntFunction.class.getName()},
                {"int", "int", "applyAsInt", IntUnaryOperator.class.getName()},
                {"long", "int", "applyAsInt", LongToIntFunction.class.getName()},
                {"T", "int", "applyAsInt", ToIntFunction.class.getName()},
                {"double", "long", "applyAsLong", DoubleToLongFunction.class.getName()},
                {"int", "long", "applyAsLong", IntToLongFunction.class.getName()},
                {"long", "long", "applyAsLong", LongUnaryOperator.class.getName()},
                {"T", "long", "applyAsLong", ToLongFunction.class.getName()},
                {"double", "R", "apply", DoubleFunction.class.getName()},
                {"int", "R", "apply", IntFunction.class.getName()},
                {"long", "R", "apply", LongFunction.class.getName()},
                {"T", "R", "apply", Function.class.getName()},
                {"T", "T", "apply", UnaryOperator.class.getName()},
                {"double, double", "double", "applyAsDouble", DoubleBinaryOperator.class.getName()},
                {"T, U", "double", "applyAsDouble", ToDoubleBiFunction.class.getName()},
                {"int, int", "int", "applyAsInt", IntBinaryOperator.class.getName()},
                {"T, U", "int", "applyAsInt", ToIntBiFunction.class.getName()},
                {"long, long", "long", "applyAsLong", LongBinaryOperator.class.getName()},
                {"T, U", "long", "applyAsLong", ToLongBiFunction.class.getName()},
                {"T, U", "R", "apply", BiFunction.class.getName()},
                {"T, T", "T", "apply", BinaryOperator.class.getName()},
                {"", "T", "get", Supplier.class.getName()},
                {"", "boolean", "getAsBoolean", BooleanSupplier.class.getName()},
                {"", "double", "getAsDouble", DoubleSupplier.class.getName()},
                {"", "int", "getAsInt", IntSupplier.class.getName()},
                {"", "long", "getAsLong", LongSupplier.class.getName()},
                {"double", "boolean", "test", DoublePredicate.class.getName()},
                {"int", "boolean", "test", IntPredicate.class.getName()},
                {"long", "boolean", "test", LongPredicate.class.getName()},
                {"T", "boolean", "test", Predicate.class.getName()},
                {"T, U", "boolean", "test", BiPredicate.class.getName()}
        });
    }

    @Test
    public void generates_method_names() {
        assertThat(generator.getMethodName(), is(expectedMethodName));
    }

    @Test
    public void generates_class_names() {
        assertThat(generator.getClassName(), is(withoutPackage(expectedClassName)));
    }

    private static String withoutPackage(String className) {
        return className.substring(className.lastIndexOf(".") + 1);
    }

    @Test
    public void examples_match_the_Java_library() throws Exception {
        Class<?> clazz = Class.forName(expectedClassName);
        Method method = clazz.getMethod(expectedMethodName, generator.getParameterTypes());
        assertThat("return type", method.getReturnType(), is(equalTo(generator.getReturnType())));
    }
}
