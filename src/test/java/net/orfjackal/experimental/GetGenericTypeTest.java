package net.orfjackal.experimental;

import org.junit.Test;

import java.lang.reflect.*;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Esko Luontola
 * @since 16.8.2011
 */
public class GetGenericTypeTest {

    @Test
    public void get_generic_type_of_a_field() throws Exception {
        Field field = Dummy.class.getDeclaredField("stringListField");
        ParameterizedType type = (ParameterizedType) field.getGenericType();

        assertEquals(List.class, type.getRawType());
        assertArrayEquals(new Type[]{String.class}, type.getActualTypeArguments());
        assertEquals(null, type.getOwnerType());
    }

    @Test
    public void get_generic_type_of_class_method_parameters() throws Exception {
        Method method = Dummy.class.getDeclaredMethod("stringListMethod", List.class);
        Type[] types = method.getGenericParameterTypes();
        ParameterizedType type = (ParameterizedType) types[0];

        assertEquals(List.class, type.getRawType());
        assertArrayEquals(new Type[]{String.class}, type.getActualTypeArguments());
        assertEquals(null, type.getOwnerType());
    }

    @Test
    public void get_generic_type_of_interface_method_parameters() throws Exception {
        Method method = Interface.class.getDeclaredMethod("stringListMethod", List.class);
        Type[] types = method.getGenericParameterTypes();
        ParameterizedType type = (ParameterizedType) types[0];

        assertEquals(List.class, type.getRawType());
        assertArrayEquals(new Type[]{String.class}, type.getActualTypeArguments());
        assertEquals(null, type.getOwnerType());
    }


    @SuppressWarnings({"UnusedDeclaration"})
    public class Dummy {
        public List<String> stringListField;

        public void stringListMethod(List<String> list) {
        }
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public interface Interface {
        void stringListMethod(List<String> list);
    }
}
