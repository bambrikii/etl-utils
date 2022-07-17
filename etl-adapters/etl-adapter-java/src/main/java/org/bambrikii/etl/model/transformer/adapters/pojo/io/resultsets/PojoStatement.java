package org.bambrikii.etl.model.transformer.adapters.pojo.io.resultsets;

import org.bambrikii.etl.model.transformer.adapters.EtlRuntimeException;
import org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors.PojoFieldDescriptor;
import org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors.PojoFieldDescriptorsContainer;
import org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors.PojoWriteCursor;
import org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors.PojoWriteCursorsContainer;
import org.bambrikii.etl.model.transformer.adapters.pojo.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PojoStatement {
    private final Object target;
    private final PojoFieldDescriptorsContainer fieldDescriptors;
    private final PojoWriteCursorsContainer cursorsContainer;

    public PojoStatement() {
        this(new HashMap<>());
    }

    public PojoStatement(Object target) {
        this.target = target == null ? new HashMap<>() : target;
        fieldDescriptors = new PojoFieldDescriptorsContainer();
        cursorsContainer = new PojoWriteCursorsContainer();
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
        PojoFieldDescriptor fieldDescriptor = fieldDescriptors.ensureFieldDescriptor(fullName, namePos);
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

    private boolean tryWriteListValue(Object obj, String listPrefix, String fullName, PojoFieldDescriptor fieldDescriptor, Object value, int namePos, Class<?> valueCls) {
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
        PojoWriteCursor cursor = cursorsContainer.ensureCursor(fieldDescriptor);
        if (distinctName.equals(listPrefix)) {
            list.add(fieldObj);
            cursor.advanceSize();
        } else {
            int currentPosition = cursor.getCurrentPosition();
            list.add(currentPosition, fieldObj);
        }
        setValue0(fieldObj, listPrefix, fullName, value, namePos + 1, valueCls);
        return true;
    }

    private boolean tryWriteMapValue(Object obj, String fullName, Object value, int namePos, Class<?> valueCls, String listPrefix, PojoFieldDescriptor fieldDescriptor) {
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

    private Object tryCreateInstance(PojoFieldDescriptor fieldDescriptor) {
        if (fieldDescriptor.isArray()) {
            return new ArrayList<>();
        }
        Class<?> cls = fieldDescriptor.getType();
        if (cls != null) {
            return ReflectionUtils.tryNewInstance(cls);
        }
        if (cls == null) { // TODO: is it conditional? can there be other options?
            return new HashMap<>();
        }
        throw new EtlRuntimeException("Cannot determine type for [" + fieldDescriptor.getDistinctName() + "] field!");
    }

    private boolean tryWriteReflectiveValue(Object obj, String fullName, Object value, int namePos, Class<?> valueCls, String listPrefix, PojoFieldDescriptor fieldDescriptor) {
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
            throw new EtlRuntimeException("Final attempt to set value with reflection: failed to find property [" + fullName + "] : [" + fieldDescriptor.toString() + "]!");
        }
        setValue0(fieldObj, listPrefix, fullName, value, namePos + 1, valueCls);
        return true;
    }
}
