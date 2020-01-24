package org.bambrikii.etl.model.transformer.adapters.java.maptree.io;

import org.bambikii.etl.model.transformer.adapters.EtlRuntimeException;
import org.bambrikii.etl.model.transformer.adapters.java.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bambrikii.etl.model.transformer.adapters.java.maptree.io.FieldDescriptor.NOT_AVAILABLE_FIELD_DESCRIPTOR;

public class EtlTreeResultSet {
    private final Object source;
    private final FieldDescriptorsContainer fieldDescriptors;

    private final Map<String, Cursor> cursors;
    private Cursor currentCursor;

    public EtlTreeResultSet(Object source) {
        this.source = source;
        fieldDescriptors = new FieldDescriptorsContainer();
        cursors = new HashMap<>();
    }

    public <T> T getObject(String name, Class<T> valueClass) {
        return (T) readObject0(name, valueClass);
    }

    private Object readObject0(String name, Class<?> valueClass) {
        return readObject(source, name, 0, valueClass);
    }

    private Object readObject(Object obj, String fullName, int namePos, Class<?> valueClass) {
        if (obj == null) {
            return null;
        }
        FieldDescriptor fieldDescriptor = fieldDescriptors.getFieldDescriptor(fullName, namePos);
        if (NOT_AVAILABLE_FIELD_DESCRIPTOR.equals(fieldDescriptor)) {
            return obj;
        }
        if (obj instanceof List) {
            return tryReadList(fullName, namePos, fieldDescriptor, (List) obj, valueClass);
        }
        if (obj instanceof Map) {
            return tryReadMap((Map<String, Object>) obj, fullName, namePos, fieldDescriptor, valueClass);
        }
        return tryReadReflective(obj, namePos, fieldDescriptor, valueClass);
    }

    private Object tryReadReflective(Object obj, int namePos, FieldDescriptor fieldDescriptor, Class<?> valueClass) {
        // TODO: Try reflection
        Method getter = ReflectionUtils.findGetter(obj, fieldDescriptor.getSimpleName(), valueClass);
        throw new EtlRuntimeException("Type [" + obj.getClass().getName() + "] for pos [" + namePos + "] is not readable for " + fieldDescriptor + "!");
    }

    private Object tryReadMap(Map<String, Object> obj, String fullName, int namePos, FieldDescriptor fieldDescriptor, Class<?> valueClass) {
        Object obj2 = obj.get(fieldDescriptor.getSimpleName());
        return readObject(obj2, fullName, namePos + 1, valueClass);
    }

    private Object tryReadList(String fullName, int namePos, FieldDescriptor fieldDescriptor, List list, Class<?> valueClass) {
        Cursor cursor = ensureCursor(fieldDescriptor, list.size() - 1);
        int pos = cursor.getCurrentPosition();
        if (!cursor.canRead()) {
            throw new EtlRuntimeException("List element " + (list.size() - 1) + " < [" + pos + "] for field " + fieldDescriptor + "!");
        }
        Object listElem = list.get(pos);
        return readObject(listElem, fullName, namePos + 1, valueClass);
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
            // TODO: review the case when currentCursor is null
            return true;
        }
        boolean next = currentCursor.next();
        if (next) {
            return true;
        }
        currentCursor = currentCursor.getParentCursor();
        return false;
    }
}
