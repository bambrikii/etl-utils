package org.bambrikii.etl.model.transformer.adapters.java.maptree.io;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bambrikii.etl.model.transformer.adapters.java.maptree.io.FieldDescriptorsContainer.splitFields;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NameSplitTest {
    public static final String NAME = "(\\.|^|)([^.]*)((<.*>)|(\\[\\])|)(\\.|$|)";
    private String str = "string.sTring<str.str.str>.strin_g.string.qwe.zzxc[]<qwe>.qwe..[].3234.wer";

    private void print(List<FieldNameElement> fields) {
        for (FieldNameElement field : fields) {
            System.out.println(": " + field.getName() + "<" + field.getType() + ">");
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

        List<FieldNameElement> fields = splitFields(str);

        print(fields);
    }

    @Test
    public void shouldSplitNestedLists() {
        List<FieldNameElement> fields = splitFields("list[]..");

        print(fields);

        assertEquals(3, fields.size());
    }

    @Test
    public void shouldSplitWithTypes() {
        List<FieldNameElement> fields = splitFields("sTring<str.str.str>.zzxc[]<qwe>");

        assertEquals(2, fields.size());

        FieldNameElement nameElement1 = fields.get(0);
        assertEquals("sTring", nameElement1.getName());
        assertEquals("str.str.str", nameElement1.getType());

        FieldNameElement nameElement2 = fields.get(1);
        assertEquals("zzxc[]", nameElement2.getName());
        assertEquals("qwe", nameElement2.getType());
    }
}
