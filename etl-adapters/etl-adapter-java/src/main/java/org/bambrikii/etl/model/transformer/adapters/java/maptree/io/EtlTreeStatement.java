package org.bambrikii.etl.model.transformer.adapters.java.maptree.io;

import org.bambikii.etl.model.transformer.adapters.EtlRuntimeException;
import org.bambrikii.etl.model.transformer.adapters.java.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EtlTreeStatement {
    private final Object target;
    private final FieldDescriptorsContainer fieldDescriptors;
    private final CursorsContainer cursorsContainer;

    public EtlTreeStatement() {
        this(new HashMap<>());
    }

    public EtlTreeStatement(Object target) {
        this.target = target == null ? new HashMap<>() : target;
        fieldDescriptors = new FieldDescriptorsContainer();
        cursorsContainer = new CursorsContainer();
    }

    public Object getTarget() {
        return target;
    }

    public void setField(String name, Object value) {
        Objects.requireNonNull(value);

        setValue0(target, null, name, value, 0, value.getClass());
    }

    public void setField(String listPrefix, String name, Object value) {
        Objects.requireNonNull(value);

        setValue0(target, listPrefix, name, value, 0, value.getClass());
    }

    public void setField(String name, Object value, Class<?> valueCls) {
        setValue0(target, null, name, value, 0, valueCls);
    }

    public void setField(String listPrefix, String name, Object value, Class<?> valueCls) {
        setValue0(target, listPrefix, name, value, 0, valueCls);
    }

    private void setValue0(Object obj, String listPrefix, String fullName, Object value, int namePos, Class<?> valueCls) {
        if (obj == null) {
            return;
        }
        FieldDescriptor fieldDescriptor = fieldDescriptors.ensureFieldDescriptor(fullName, namePos);
        if (tryWriteListValue(obj, listPrefix, fullName, fieldDescriptor, value, namePos, valueCls)) {
            return;
        }
        if (tryWriteMapValue(obj, fullName, value, namePos, valueCls, listPrefix, fieldDescriptor)) {
            return;
        }
        if (tryWriteReflectiveValue(obj, fullName, value, namePos, valueCls, listPrefix, fieldDescriptor)) {
            return;
        }
        throw new EtlRuntimeException("Failed to find property [" + fullName + ":" + namePos + ":" + valueCls.getName() + "] for object " + obj + "");
    }

    private boolean tryWriteListValue(Object obj, String listPrefix, String fullName, FieldDescriptor fieldDescriptor, Object value, int namePos, Class<?> valueCls) {
        if (!(obj instanceof List)) {
            return false;
        }

        List list = (List) obj;
        if (fieldDescriptor.isLeaf()) {
            list.add(value);
            return true;
        }

        Object fieldObj = tryCreateInstance(fieldDescriptor);
        String distinctName = fieldDescriptor.getDistinctName();
        if (distinctName.equals(listPrefix)) {
            Cursor cursor = cursorsContainer.ensureCursor(fieldDescriptor);
            list.add(fieldObj);
            cursor.next();
        } else {
            Cursor cursor = cursorsContainer.ensureCursor(fieldDescriptor);
            int currentPosition = cursor.getCurrentPosition();
            list.add(currentPosition, fieldObj);
        }
        setValue0(fieldObj, listPrefix, fullName, value, namePos + 1, valueCls);
        return true;
    }

    private boolean tryWriteMapValue(Object obj, String fullName, Object value, int namePos, Class<?> valueCls, String listPrefix, FieldDescriptor fieldDescriptor) {
        if (!(obj instanceof Map)) {
            return false;
        }
        String fieldName = fieldDescriptor.getSimpleName();
        if (fieldDescriptor.isLeaf()) {
            ((Map) obj).put(fieldName, value);
            return true;
        }
        Object fieldObj;
        if (((Map) obj).containsKey(fieldName)) {
            fieldObj = ((Map<String, Object>) obj).get(fieldName);
        } else {
            fieldObj = tryCreateInstance(fieldDescriptor);
            ((Map) obj).put(fieldName, fieldObj);
        }
        setValue0(fieldObj, listPrefix, fullName, value, namePos + 1, valueCls);
        return true;
    }

    private Object tryCreateInstance(FieldDescriptor fieldDescriptor) {
        if (fieldDescriptor.isArray()) {
            return new ArrayList<>();
        }
        Class<?> cls = fieldDescriptor.getType();
        if (cls != null) {
            return ReflectionUtils.tryNewInstance(cls);
        }
        throw new EtlRuntimeException("Cannot determine type for [" + fieldDescriptor.getDistinctName() + "] field!");
    }

    private boolean tryWriteReflectiveValue(Object obj, String fullName, Object value, int namePos, Class<?> valueCls, String listPrefix, FieldDescriptor fieldDescriptor) {
        String fieldName = fieldDescriptor.getSimpleName();
        if (fieldDescriptor.isLeaf()) {
            Method setter = ReflectionUtils.findSetter(obj, fieldName, valueCls);
            if (setter == null) {
                return false;
            }
            ReflectionUtils.setValue(setter, obj, value);
            return true;
        }
        Method getter = ReflectionUtils.findGetter(obj, fieldName);
        Object fieldObj = ReflectionUtils.getValue(getter, obj);
        if (fieldObj == null) {
            // TODO: Should create object based on return type
            throw new EtlRuntimeException("Failed to find property " + fullName + " : " + fieldDescriptor.toString());
        }
        setValue0(fieldObj, listPrefix, fullName, value, namePos + 1, valueCls);
        return true;
    }
}
