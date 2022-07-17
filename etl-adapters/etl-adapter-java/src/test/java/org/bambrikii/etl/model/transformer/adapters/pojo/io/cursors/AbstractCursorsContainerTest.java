package org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors;

import org.bambrikii.etl.model.transformer.cursors.AbstractCursor;
import org.bambrikii.etl.model.transformer.cursors.AbstractCursorsContainer;
import org.bambrikii.etl.model.transformer.cursors.AbstractFieldDescriptor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbstractCursorsContainerTest {
    @Test
    public void shouldAdvanceSingleCursor() {
        AbstractCursorsContainer container = new PojoReadCursorsContainer();
        PojoFieldDescriptor parentDescriptor = new PojoFieldDescriptor("field1", "field1", 0, false, false, null);
        AbstractFieldDescriptor descriptor = new PojoFieldDescriptor("field2", "field1.field2", 1, true, true, parentDescriptor);

        AbstractCursor cursor = container.ensureCursor(descriptor, 2, null);

        assertTrue(container.next());
        assertFalse(container.next());
    }

    @Test
    public void shouldAdvanceNestedCursors() {
        AbstractCursorsContainer container = new PojoReadCursorsContainer();
        PojoFieldDescriptor parentDescriptor = new PojoFieldDescriptor("field1", "field1", 0, true, false, null);
        AbstractFieldDescriptor descriptor = new PojoFieldDescriptor("field2", "field1.field2", 1, true, true, parentDescriptor);

        AbstractCursor parentCursor = container.ensureCursor(parentDescriptor, 2, null);
        AbstractCursor cursor = container.ensureCursor(descriptor, 2, parentCursor);

        assertTrue(container.next());
        assertTrue(container.next());
        assertFalse(container.next());
    }

    @Test
    public void shouldAdvanceConcurrentCursors() {
        AbstractCursorsContainer container = new PojoReadCursorsContainer();
        AbstractFieldDescriptor descriptor1 = new PojoFieldDescriptor("field1", "field1", 0, true, false, null);
        AbstractFieldDescriptor descriptor2 = new PojoFieldDescriptor("field2", "field2", 0, true, true, null);

        AbstractCursor cursor1 = container.ensureCursor(descriptor1, 2, null);
        AbstractCursor cursor2 = container.ensureCursor(descriptor2, 2, null);

        assertTrue(container.next());
        assertTrue(container.next());
        assertFalse(container.next());
    }


    @Test
    public void shouldAdvanceConcurrentNestedCursors() {
        AbstractCursorsContainer container = new PojoReadCursorsContainer();
        PojoFieldDescriptor parentDescriptor = new PojoFieldDescriptor("parentField1", "parentField1", 0, true, false, null);
        AbstractFieldDescriptor descriptor1 = new PojoFieldDescriptor("field1", "parentField1.field1", 0, true, false, parentDescriptor);
        AbstractFieldDescriptor descriptor2 = new PojoFieldDescriptor("field2", "parentField1.field2", 0, true, true, parentDescriptor);

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
