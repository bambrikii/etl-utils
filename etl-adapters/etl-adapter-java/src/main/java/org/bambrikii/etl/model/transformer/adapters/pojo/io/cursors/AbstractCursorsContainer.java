package org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors;

import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class AbstractCursorsContainer<T extends AbstractCursor<T>> {
    private final Map<String, T> byDistinctName = new LinkedHashMap<>();
    private final Deque<T> queue = new LinkedList<>();

    public T ensureCursor(FieldDescriptor fieldDescriptor, int size, T parentCursor) {
        String distinctName = fieldDescriptor.getDistinctName();
        if (byDistinctName.containsKey(distinctName)) {
            return byDistinctName.get(distinctName);
        }
        T cursor = createCursor(fieldDescriptor, size, parentCursor);
        byDistinctName.put(distinctName, cursor);
        queue.addLast(cursor);
        return cursor;
    }

    protected abstract T createCursor(FieldDescriptor fieldDescriptor, int size, T parentCursor);

    public boolean next() {
        while (!queue.isEmpty()) {
            T cursor = queue.peekLast();
            if (cursor.hasNext()) {
                return cursor.next();
            }
            queue.pollLast();
            byDistinctName.remove(cursor.getFieldDescriptor().getDistinctName());
            T parent = cursor.getParentCursor();
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

    public T ensureCursor(FieldDescriptor descriptor) {
        if (!descriptor.isArray()) {
            return null;
        }
        return ensureCursor(descriptor, -1, findParentCursor(descriptor));
    }

    private T findParentCursor(FieldDescriptor descriptor) {
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
