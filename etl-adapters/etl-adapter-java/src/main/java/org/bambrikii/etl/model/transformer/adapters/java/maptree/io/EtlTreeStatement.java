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

    public EtlTreeStatement() {
        this(new HashMap<>());
    }

    public EtlTreeStatement(Object target) {
        this.target = target == null ? new HashMap<>() : target;
        fieldDescriptors = new FieldDescriptorsContainer();
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
        if (tryWriteListValue(obj, value, listPrefix, fieldDescriptor)) {
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

    private boolean tryWriteListValue(Object obj, Object value, String listPrefix, FieldDescriptor fieldDescriptor) {
        if (!(obj instanceof List)) {
            return false;
        }

        List list = (List) obj;
        if (fieldDescriptor.isLeaf()) {
            list.add(value);
        } else {
            // TODO: set value using cursor
            String distinctName = fieldDescriptor.getDistinctName();
            if (list.size() == 0 || distinctName.equals(listPrefix)) {
                Object newObj;
                if (fieldDescriptor.isArray()) {
                    newObj = new ArrayList<>();
                } else {
                    Class<?> cls = fieldDescriptor.getType();
                    if (cls == null) {
                        throw new EtlRuntimeException("Class or array flag is expected for [" + distinctName + "] field.");
                    }
                    newObj = ReflectionUtils.tryNewInstance(cls);
                }
                list.add(newObj);
            }
        }
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
            // TODO: should create object of some specific type
            fieldDescriptor.getType();
            fieldObj = new HashMap<>();
            ((Map) obj).put(fieldName, fieldObj);
        }
        setValue0(fieldObj, listPrefix, fullName, value, namePos + 1, valueCls);
        return true;
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
