package org.bambrikii.etl.model.transformer.adapters.java;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArrayEqualTest {
    @Test
    public void shouldBeEqual() {
        List<String> arr1 = Arrays.asList("a", "b", "c");
        List<String> arr2 = Arrays.asList("a", "b", "c");

        assertTrue(arr1.equals(arr2));
    }
}
