package org.bambrikii.etl.model.transformer.adapters.java.maptree.io;

import org.bambikii.etl.model.transformer.adapters.EtlRuntimeException;
import org.bambrikii.etl.model.transformer.adapters.java.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EtlTreeStatement {
    private final Object target;
    private final FieldDescriptorsContainer fieldDescriptors;
    private final Map<String, Cursor> cursors;
    private Cursor currentCursor;

    public EtlTreeStatement(Object target) {
        this.target = target;
        fieldDescriptors = new FieldDescriptorsContainer();
        cursors = new HashMap<>();
    }

    public void setField(String name, Object value, Class<?> valueCls) {
        setValue0(target, name, value, 0, valueCls);
    }

    private void setValue0(Object obj, String fullName, Object value, int namePos, Class<?> valueCls) {
        FieldDescriptor fieldDescriptor = fieldDescriptors.getFieldDescriptor(fullName, namePos);
        if (tryWriteListValue(obj, fieldDescriptor)) {
            return;
        }
        String fieldName = fieldDescriptor.getSimpleName();
        if (tryWriteMapValue(obj, fullName, value, namePos, valueCls, fieldDescriptor, fieldName)) {
            return;
        }
        if (tryWriteReflectiveValue(obj, fullName, value, namePos, valueCls, fieldDescriptor, fieldName)) {
            return;
        }
    }

    private boolean tryWriteListValue(Object obj, FieldDescriptor fieldDescriptor) {
        if (!(obj instanceof List)) {
            return false;
        }

        if (fieldDescriptor.isLeaf()) {
            // TODO: set value using cursor
            ensureCursor(fieldDescriptor);
        } else {
            // iterate using cursor
        }
        return true;
    }

    private Cursor ensureCursor(FieldDescriptor fieldDescriptor) {
        String distinctName = fieldDescriptor.getDistinctName();
        if (cursors.containsKey(distinctName)) {
            return cursors.get(distinctName);
        }
        Cursor cursor = new Cursor(fieldDescriptor, -1, currentCursor);
        cursors.put(distinctName, cursor);
        currentCursor = cursor
        return cursor;
    }

    private boolean tryWriteMapValue(Object obj, String fullName, Object value, int namePos, Class<?> valueCls, FieldDescriptor fieldDescriptor, String fieldName) {
        if (!(obj instanceof Map)) {
            return false;
        }
        if (fieldDescriptor.isLeaf()) {
            ((Map) obj).put(fieldName, value);
            return true;
        }
        Map<String, Object> fieldValue;
        if (((Map) obj).containsKey(fieldName)) {
            fieldValue = (Map<String, Object>) ((Map) obj).get(fieldName);
        } else {
            fieldValue = new HashMap<>();
            ((Map) obj).put(fieldName, fieldValue);
        }
        setValue0(fieldValue, fullName, value, namePos + 1, valueCls);
        return true;
    }

    private boolean tryWriteReflectiveValue(Object obj, String fullName, Object value, int namePos, Class<?> valueCls, FieldDescriptor fieldDescriptor, String fieldName) {
        if (fieldDescriptor.isLeaf()) {
            Method setter = ReflectionUtils.findSetter(obj, fieldName);
            if (setter == null) {
                throw new EtlRuntimeException("Failed to find setter for " + fullName + "");
            }
            ReflectionUtils.setValue(setter, obj, value);
        }
        Method getter = ReflectionUtils.findGetter(obj, fieldName, valueCls);
        Object fieldValue = ReflectionUtils.getValue(getter, obj);
        if (fieldValue == null) {
            throw new EtlRuntimeException("Failed to find property " + fullName + " : " + fieldDescriptor.toString());
        }
        setValue0(fieldValue, fullName, value, namePos + 1, valueCls);
        return true;
    }
}
