package org.bambrikii.etl.model.transformer.adapters.java.maptree.io;

import org.bambikii.etl.model.transformer.adapters.EtlRuntimeException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EtlTreeResultSet {
    private final Object source;
    private final FieldDescriptorsContainer fieldDescriptors;

    private final Map<String, Cursor> cursors;
    private final Map<Integer, String> cursorNames;
    private Cursor currentCursor;

    public EtlTreeResultSet(Object source) {
        this.source = source;
        cursors = new HashMap<>();
        cursorNames = new HashMap<>();
        fieldDescriptors = new FieldDescriptorsContainer();
    }

    public String getString(String name) {
        return (String) readObject0(name);
    }

    public Integer getInteger(String name) {
        return (Integer) readObject0(name);
    }

    public Double getDouble(String name) {
        return (Double) readObject0(name);
    }

    private Object readObject0(String name) {
        return readObject(source, name, 0);
    }

    private Object readObject(Object obj, String fullName, int namePos) {
        if (obj == null) {
            return null;
        }
        FieldDescriptor fieldDescriptor = fieldDescriptors.getFieldDescriptor(fullName, namePos);
        if (obj instanceof List) {
            List list = (List) obj;
            Cursor cursor = ensureCursor(fieldDescriptor, list.size() - 1);
            int pos = cursor.getCurrentPosition();
            if (!cursor.canRead()) {
                throw new EtlRuntimeException("List element " + (list.size() - 1) + " < [" + pos + "] for field " + fieldDescriptor + "!");
            }
            Object listElem = list.get(pos);
            return readObject(listElem, fullName, namePos + 1);
        }
        if (obj instanceof Map) {
            Object obj2 = ((Map<String, Object>) obj).get(fieldDescriptor.getSimpleName());
            return readObject(obj2, fullName, namePos + 1);
        }
        // TODO: Try reflection
        throw new EtlRuntimeException("Type [" + obj.getClass().getName() + "] for pos [" + namePos + "] is not readable for " + fieldDescriptor + "!");
    }

    private void validateCurrentCursor(FieldDescriptor fieldDescriptor) {
        if (currentCursor == null) {
            return;
        }
        FieldDescriptor currentCursorFieldDescriptor = currentCursor.getFieldDescriptor();
        if (currentCursorFieldDescriptor.getSimpleNamePosition() == fieldDescriptor.getSimpleNamePosition() &&
                currentCursorFieldDescriptor.getDistinctName().equals(fieldDescriptor.getDistinctName())
        ) {
            throw new EtlRuntimeException("Field cursors [" + currentCursorFieldDescriptor.getDistinctName() + "] and [" + fieldDescriptor.getDistinctName() + "] cannot be used simultaneously!");
        }
    }

    private Cursor ensureCursor(FieldDescriptor fieldDescriptor, int size) {
        String distinctName = fieldDescriptor.getDistinctName();
        if (cursors.containsKey(distinctName)) {
            return cursors.get(distinctName);
        }
        validateCurrentCursor(fieldDescriptor);
        Cursor cursor = new Cursor(fieldDescriptor, size, currentCursor);
        cursors.put(distinctName, cursor);
        currentCursor = cursor;
        return cursor;
    }

    public boolean next() {
        if (currentCursor == null) {
            return false;
        }
        boolean next = currentCursor.next();
        if (next) {
            return true;
        }
        currentCursor = currentCursor.getParentCursor();
        return false;
    }
}
