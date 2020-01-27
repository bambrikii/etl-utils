package org.bambrikii.etl.model.transformer.adapters.java.maptree.io;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bambrikii.etl.model.transformer.adapters.java.maptree.io.FieldDescriptorsContainer.splitFields;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NameSplitTest {
    public static final String NAME = "(\\.|^|)([^.]*)((<.*>)|(\\[\\])|)(\\.|$|)";
    private String str = "string.sTring<str.str.str>.strin_g.string.qwe.zzxc[].qwe..[].3234.wer";

    private void print(List<String> fields) {
        for (String field : fields) {
            System.out.println(": " + field);
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
    public void shouldSplit2() {
        String str = this.str;

        List<String> fields = splitFields(str);

        print(fields);
    }

    @Test
    public void shouldSplit3() {
        List<String> fields = splitFields("list[]..");

        print(fields);

        assertEquals(3, fields.size());
    }
}
