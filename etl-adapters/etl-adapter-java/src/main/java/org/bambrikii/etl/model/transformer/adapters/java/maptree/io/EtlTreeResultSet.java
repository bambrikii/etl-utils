package org.bambrikii.etl.model.transformer.adapters.java.maptree.io;

import org.bambrikii.etl.model.transformer.adapters.java.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.bambrikii.etl.model.transformer.adapters.java.maptree.io.FieldDescriptorsContainer.NOT_AVAILABLE_FIELD_DESCRIPTOR;
import static org.bambrikii.etl.model.transformer.adapters.java.maptree.io.ReadStatusEnum.LIST_READ_FAILED;

public class EtlTreeResultSet {
    private final Object source;
    private final FieldDescriptorsContainer fieldDescriptors;
    private final CursorsContainer cursorsContainer;
    private final StatusesContainer statusesContainer;

    public EtlTreeResultSet(Object source) {
        this.source = source;
        fieldDescriptors = new FieldDescriptorsContainer();
        cursorsContainer = new CursorsContainer();
        statusesContainer = new StatusesContainer();
    }

    public <T> T getObject(String name, Class<T> valueClass) {
        return (T) readObject0(name, valueClass);
    }

    private Object readObject0(String name, Class<?> valueClass) {
        return readObject(source, name, 0, valueClass, null);
    }

    private Object readObject(Object obj, String fullName, int namePos, Class<?> valueClass, Cursor parentCursor) {
        if (obj == null) {
            return null;
        }
        FieldDescriptor fieldDescriptor = fieldDescriptors.ensureFieldDescriptor(fullName, namePos);
        if (NOT_AVAILABLE_FIELD_DESCRIPTOR.equals(fieldDescriptor)) {
            return obj;
        }
        if (obj instanceof List) {
            return tryReadList(fullName, namePos, fieldDescriptor, (List) obj, valueClass, parentCursor);
        }
        if (obj instanceof Map) {
            return tryReadMap((Map<String, Object>) obj, fullName, namePos, fieldDescriptor, valueClass, parentCursor);
        }
        return tryReadReflective(obj, fullName, namePos, fieldDescriptor, valueClass, parentCursor);
    }

    private Object tryReadList(String fullName, int namePos, FieldDescriptor fieldDescriptor, List list, Class<?> valueClass, Cursor parentCursor) {
        Cursor cursor = cursorsContainer.ensureCursor(fieldDescriptor, list.size(), parentCursor);
        if (!cursor.canRead()) {
            String message = "List element " + (list.size() - 1) + " < [" + cursor.getSize() + "] for field " + fieldDescriptor + "!";
            statusesContainer.addStatus(LIST_READ_FAILED, message);
            return null;
        }
        int pos = cursor.getCurrentPosition();
        Object listElem = list.get(pos);
        return readObject(listElem, fullName, namePos + 1, valueClass, parentCursor);
    }

    private Object tryReadMap(Map<String, Object> obj, String fullName, int namePos, FieldDescriptor fieldDescriptor, Class<?> valueClass, Cursor parentCursor) {
        Object obj2 = obj.get(fieldDescriptor.getSimpleName());
        return readObject(obj2, fullName, namePos + 1, valueClass, parentCursor);
    }

    private Object tryReadReflective(Object obj, String fullName, int namePos, FieldDescriptor fieldDescriptor, Class<?> valueClass, Cursor parentCursor) {
        Method getter = ReflectionUtils.findGetter(obj, fieldDescriptor.getSimpleName());
        Object value = ReflectionUtils.getValue(getter, obj);
        return readObject(value, fullName, namePos + 1, valueClass, parentCursor);
    }

    public boolean isStatusValid() {
        return statusesContainer.isValid();
    }

    public List<ReadStatus> statuses() {
        return statusesContainer.getStatuses();
    }

    public boolean next() {
        statusesContainer.clearStatuses();
        return cursorsContainer.next();
    }
}
