package org.bambrikii.etl.model.transformer.adapters.java.maptree.io;

import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class CursorsContainer {
    private final Map<String, Cursor> byDistinctName = new LinkedHashMap<>();
    private final Deque<Cursor> queue = new LinkedList<>();

    public Cursor ensureCursor(FieldDescriptor fieldDescriptor, int size, Cursor parentCursor) {
        String distinctName = fieldDescriptor.getDistinctName();
        if (byDistinctName.containsKey(distinctName)) {
            return byDistinctName.get(distinctName);
        }
        Cursor cursor = new Cursor(fieldDescriptor, size, parentCursor);
        byDistinctName.put(distinctName, cursor);
        queue.addLast(cursor);
        return cursor;
    }

    public boolean next() {
        while (!queue.isEmpty()) {
            Cursor cursor = queue.peekLast();
            if (cursor.hasNext()) {
                return cursor.next();
            }
            queue.pollLast();
            byDistinctName.remove(cursor.getFieldDescriptor().getDistinctName());
            Cursor parent = cursor.getParentCursor();
            if (parent != null) {
                String parentDistinctName = parent.getFieldDescriptor().getDistinctName();
                if (!byDistinctName.containsKey(parentDistinctName)) {
                    queue.addFirst(parent);
                    byDistinctName.put(parentDistinctName, parent);
                }
            }
        }
        return false;
    }

    public Cursor ensureCursor(FieldDescriptor descriptor) {
        if (!descriptor.isArray()) {
            return null;
        }
        Cursor parentCursor = findParentCursor(descriptor);
        return ensureCursor(descriptor, -1, parentCursor);
    }

    private Cursor findParentCursor(FieldDescriptor descriptor) {
        if (descriptor == null) {
            return null;
        }
        FieldDescriptor parent = descriptor.getParent();
        if (parent == null) {
            return null;
        }
        if (parent.isArray()) {
            return ensureCursor(parent);
        }
        return findParentCursor(parent);
    }
}
