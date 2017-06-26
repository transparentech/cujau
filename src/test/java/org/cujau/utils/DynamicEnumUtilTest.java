package org.cujau.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DynamicEnumUtilTest {
    private enum TestEnum {
        A, B, C;
    }

    @Test
    public void testAddEnum() {
        assertEquals(3, TestEnum.values().length);

        DynamicEnumUtil.addEnum(TestEnum.class, "D");
        DynamicEnumUtil.addEnum(TestEnum.class, "E");
        DynamicEnumUtil.addEnum(TestEnum.class, "F");

        assertEquals(6, TestEnum.values().length);

        assertNotNull(TestEnum.valueOf("A"));
        assertNotNull(TestEnum.valueOf("B"));
        assertNotNull(TestEnum.valueOf("C"));
        assertNotNull(TestEnum.valueOf("D"));
        assertNotNull(TestEnum.valueOf("E"));
        assertNotNull(TestEnum.valueOf("F"));

        assertEquals("A", TestEnum.valueOf("A").name());
        assertEquals("B", TestEnum.valueOf("B").name());
        assertEquals("C", TestEnum.valueOf("C").name());
        assertEquals("D", TestEnum.valueOf("D").name());
        assertEquals("E", TestEnum.valueOf("E").name());
        assertEquals("F", TestEnum.valueOf("F").name());
    }
}
