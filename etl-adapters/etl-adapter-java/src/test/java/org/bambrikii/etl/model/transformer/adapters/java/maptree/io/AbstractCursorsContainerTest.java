package org.bambrikii.etl.model.transformer.adapters.java.maptree.io;

import org.bambrikii.etl.model.transformer.adapters.java.maptree.io.cursors.AbstractCursor;
import org.bambrikii.etl.model.transformer.adapters.java.maptree.io.cursors.AbstractCursorsContainer;
import org.bambrikii.etl.model.transformer.adapters.java.maptree.io.cursors.ReadCursorsContainer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbstractCursorsContainerTest {
    @Test
    public void shouldAdvanceSingleCursor() {
        AbstractCursorsContainer container = new ReadCursorsContainer();
        FieldDescriptor parentDescriptor = new FieldDescriptor("field1", "field1", 0, false, false, null);
        FieldDescriptor descriptor = new FieldDescriptor("field2", "field1.field2", 1, true, true, parentDescriptor);

        AbstractCursor cursor = container.ensureCursor(descriptor, 2, null);

        assertTrue(container.next());
        assertFalse(container.next());
    }

    @Test
    public void shouldAdvanceNestedCursors() {
        AbstractCursorsContainer container = new ReadCursorsContainer();
        FieldDescriptor parentDescriptor = new FieldDescriptor("field1", "field1", 0, true, false, null);
        FieldDescriptor descriptor = new FieldDescriptor("field2", "field1.field2", 1, true, true, parentDescriptor);

        AbstractCursor parentCursor = container.ensureCursor(parentDescriptor, 2, null);
        AbstractCursor cursor = container.ensureCursor(descriptor, 2, parentCursor);

        assertTrue(container.next());
        assertTrue(container.next());
        assertFalse(container.next());
    }

    @Test
    public void shouldAdvanceConcurrentCursors() {
        AbstractCursorsContainer container = new ReadCursorsContainer();
        FieldDescriptor descriptor1 = new FieldDescriptor("field1", "field1", 0, true, false, null);
        FieldDescriptor descriptor2 = new FieldDescriptor("field2", "field2", 0, true, true, null);

        AbstractCursor cursor1 = container.ensureCursor(descriptor1, 2, null);
        AbstractCursor cursor2 = container.ensureCursor(descriptor2, 2, null);

        assertTrue(container.next());
        assertTrue(container.next());
        assertFalse(container.next());
    }


    @Test
    public void shouldAdvanceConcurrentNestedCursors() {
        AbstractCursorsContainer container = new ReadCursorsContainer();
        FieldDescriptor parentDescriptor = new FieldDescriptor("parentField1", "parentField1", 0, true, false, null);
        FieldDescriptor descriptor1 = new FieldDescriptor("field1", "parentField1.field1", 0, true, false, parentDescriptor);
        FieldDescriptor descriptor2 = new FieldDescriptor("field2", "parentField1.field2", 0, true, true, parentDescriptor);

        AbstractCursor parentCursor = container.ensureCursor(parentDescriptor, 2, null);
        AbstractCursor cursor1 = container.ensureCursor(descriptor1, 2, parentCursor);
        AbstractCursor cursor2 = container.ensureCursor(descriptor2, 2, parentCursor);

        assertTrue(container.next());
        assertTrue(container.next());
        assertTrue(container.next());
        // As no reads and cursors are ensured, current cursors are getting exhausted.
//        assertTrue(container.next());
//        assertTrue(container.next());

        assertFalse(container.next());
    }
}
