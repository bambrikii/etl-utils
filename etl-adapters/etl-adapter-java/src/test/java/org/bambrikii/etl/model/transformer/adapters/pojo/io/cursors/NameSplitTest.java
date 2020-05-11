package org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class NameSplitTest {
    public static final String NAME = "(\\.|^|)([^.]*)((<.*>)|(\\[\\])|)(\\.|$|)";
    private String str = "string" +
            ".sTring<java.util.ArrayList>" +
            ".strin_g.string.qwe" +
            ".zzxc[]<TestClass1>" +
            ".qwe..[].3234.wer";

    private void print(List<FieldNameElement> fields) {
        for (FieldNameElement field : fields) {
            System.out.println(": " + field.getSimpleName() + "<" + field.getType() + ">");
        }
    }

    @Test
    public void shouldSplit() {
        Pattern pattern = Pattern.compile(NAME);
        String str = "param1..param2.";
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            System.out.println("" + matcher.group(0) + " [" + matcher.group(2) + "](" + matcher.group(3) + ")");
        }
    }

    @Test
    public void shouldSplitMultipleFields() {
        String str = this.str;

        List<FieldNameElement> fields = PojoFieldDescriptorsContainer.splitFields(str);

        print(fields);
    }

    @Test
    public void shouldSplitNestedLists() {
        List<FieldNameElement> fields = PojoFieldDescriptorsContainer.splitFields("list[]..");

        print(fields);

        assertEquals(3, fields.size());
    }

    @Test
    public void shouldSplitWithType() {
        List<FieldNameElement> fields = PojoFieldDescriptorsContainer.splitFields("sTring<java.util.ArrayList>");

        assertEquals(1, fields.size());

        FieldNameElement nameElement = fields.get(0);
        assertEquals("sTring", nameElement.getSimpleName());
        assertEquals(true, nameElement.isArray());
        assertEquals(ArrayList.class, nameElement.getType());
    }

    @Test
    public void shouldSplitWithArrayAndType() {
        List<FieldNameElement> fields = PojoFieldDescriptorsContainer.splitFields("zzxc[]<java.util.HashMap>");

        assertEquals(1, fields.size());

        FieldNameElement nameElement = fields.get(0);
        assertEquals("zzxc", nameElement.getSimpleName());
        assertEquals(true, nameElement.isArray());
        assertEquals(HashMap.class, nameElement.getType());
    }

    @Test
    public void shouldSplitWithArray() {
        List<FieldNameElement> fields = PojoFieldDescriptorsContainer.splitFields("zzxc3[]");

        assertEquals(1, fields.size());

        FieldNameElement nameElement = fields.get(0);
        assertEquals("zzxc3", nameElement.getSimpleName());
        assertEquals(true, nameElement.isArray());
        assertNull(nameElement.getType());
    }
}
