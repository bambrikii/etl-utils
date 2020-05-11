package org.bambikii.etl.model.transformer.cursors;

import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class AbstractCursorsContainer<
        K,
        T extends AbstractCursor<K, T, D>,
        D extends AbstractFieldDescriptor<K, D>
        > {
    private final Map<K, T> byFieldDistinctName = new LinkedHashMap<>();
    private final Deque<T> queue = new LinkedList<>();

    public T ensureCursor(D fieldDescriptor, int size, T parentCursor) {
        K fieldDistinctName = fieldDescriptor.getDistinctName();
        if (byFieldDistinctName.containsKey(fieldDistinctName)) {
            return byFieldDistinctName.get(fieldDistinctName);
        }
        T cursor = createCursor(fieldDescriptor, size, parentCursor);
        byFieldDistinctName.put(fieldDistinctName, cursor);
        queue.addLast(cursor);
        return cursor;
    }

    protected abstract T createCursor(D fieldDescriptor, int size, T parentCursor);

    public boolean next() {
        while (!queue.isEmpty()) {
            T cursor = queue.peekLast();
            if (cursor.hasNext()) {
                return cursor.next();
            }
            queue.pollLast();
            byFieldDistinctName.remove(cursor.getFieldDescriptor().getDistinctName());
            T parent = cursor.getParentCursor();
            if (parent != null) {
                K parentFieldDistinctName = parent.getFieldDescriptor().getDistinctName();
                if (!byFieldDistinctName.containsKey(parentFieldDistinctName)) {
                    queue.addFirst(parent);
                    byFieldDistinctName.put(parentFieldDistinctName, parent);
                }
            }
        }
        return false;
    }

    public T ensureCursor(D descriptor) {
        if (!descriptor.isArray()) {
            return null;
        }
        T parentCursor = findParentCursor(descriptor);
        T cursor = ensureCursor(descriptor, -1, parentCursor);
        return cursor;
    }

    private T findParentCursor(D descriptor) {
        if (descriptor == null) {
            return null;
        }
        D parent = descriptor.getParent();
        if (parent == null) {
            return null;
        }
        if (parent.isArray()) {
            return ensureCursor(parent);
        }
        return findParentCursor(parent);
    }
}
