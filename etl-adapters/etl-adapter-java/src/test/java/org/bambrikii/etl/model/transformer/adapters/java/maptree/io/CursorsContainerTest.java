package org.bambrikii.etl.model.transformer.adapters.java.maptree.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CursorsContainerTest {
    @Test
    public void shouldAdvanceSingleCursor() {
        CursorsContainer container = new CursorsContainer();
        FieldDescriptor parentDescriptor = new FieldDescriptor("field1", "field1", 0, false, false, null);
        FieldDescriptor descriptor = new FieldDescriptor("field2", "field1.field2", 1, true, true, parentDescriptor);

        Cursor cursor = container.ensureCursor(descriptor, 2, null);

        assertTrue(container.next());
        assertTrue(container.next());
        assertFalse(container.next());
    }

    @Test
    public void shouldAdvanceNestedCursors() {
        CursorsContainer container = new CursorsContainer();
        FieldDescriptor parentDescriptor = new FieldDescriptor("field1", "field1", 0, true, false, null);
        FieldDescriptor descriptor = new FieldDescriptor("field2", "field1.field2", 1, true, true, parentDescriptor);

        Cursor parentCursor = container.ensureCursor(parentDescriptor, 2, null);
        Cursor cursor = container.ensureCursor(descriptor, 2, parentCursor);

        assertTrue(container.next());
        assertTrue(container.next());
        assertTrue(container.next());
        assertTrue(container.next());
        assertFalse(container.next());
    }

    @Test
    public void shouldAdvanceConcurrentCursors() {
        CursorsContainer container = new CursorsContainer();
        FieldDescriptor descriptor1 = new FieldDescriptor("field1", "field1", 0, true, false, null);
        FieldDescriptor descriptor2 = new FieldDescriptor("field2", "field2", 0, true, true, null);

        Cursor cursor1 = container.ensureCursor(descriptor1, 2, null);
        Cursor cursor2 = container.ensureCursor(descriptor2, 2, null);

        assertTrue(container.next());
        assertTrue(container.next());
        assertTrue(container.next());
        assertTrue(container.next());
        assertFalse(container.next());
    }


    @Test
    public void shouldAdvanceConcurrentNestedCursors() {
        CursorsContainer container = new CursorsContainer();
        FieldDescriptor parentDescriptor = new FieldDescriptor("parentField1", "parentField1", 0, true, false, null);
        FieldDescriptor descriptor1 = new FieldDescriptor("field1", "parentField1.field1", 0, true, false, parentDescriptor);
        FieldDescriptor descriptor2 = new FieldDescriptor("field2", "parentField1.field2", 0, true, true, parentDescriptor);

        Cursor parentCursor = container.ensureCursor(parentDescriptor, 2, null);
        Cursor cursor1 = container.ensureCursor(descriptor1, 2, parentCursor);
        Cursor cursor2 = container.ensureCursor(descriptor2, 2, parentCursor);

        assertTrue(container.next());
        assertTrue(container.next());
        assertTrue(container.next());
        assertTrue(container.next());

        assertTrue(container.next());
        assertTrue(container.next());
        // As no reads and cursors are ensured, current cursors are getting exhausted.
//        assertTrue(container.next());
//        assertTrue(container.next());

        assertFalse(container.next());
    }
}
