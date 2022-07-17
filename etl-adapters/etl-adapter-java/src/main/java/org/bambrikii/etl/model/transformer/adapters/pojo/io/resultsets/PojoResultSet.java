package org.bambrikii.etl.model.transformer.adapters.pojo.io.resultsets;

import org.bambrikii.etl.model.transformer.cursors.AbstractCursor;
import org.bambrikii.etl.model.transformer.cursors.AbstractResultSet;
import org.bambrikii.etl.model.transformer.cursors.ReadStatusEnum;
import org.bambrikii.etl.model.transformer.adapters.pojo.ReflectionUtils;
import org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors.PojoFieldDescriptor;
import org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors.PojoFieldDescriptorsContainer;
import org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors.PojoReadCursor;
import org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors.PojoReadCursorsContainer;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class PojoResultSet extends AbstractResultSet<
        Object, PojoFieldDescriptorsContainer, PojoReadCursorsContainer
        > {
    public PojoResultSet(Object source) {
        super(source, new PojoFieldDescriptorsContainer(), new PojoReadCursorsContainer());
    }

    @Override
    public <T> T getObject(String name, Class<T> valueClass) {
        return (T) readObject0(name, valueClass);
    }

    private Object readObject0(String name, Class<?> valueClass) {
        return readObject(source, name, 0, valueClass, null);
    }

    private Object readObject(Object obj, String fullName, int namePos, Class<?> valueClass, PojoReadCursor parentCursor) {
        if (obj == null) {
            return null;
        }
        PojoFieldDescriptor fieldDescriptor = fieldDescriptors.ensureFieldDescriptor(fullName, namePos);
        if (PojoFieldDescriptorsContainer.NOT_AVAILABLE_FIELD_DESCRIPTOR.equals(fieldDescriptor)) {
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

    private Object tryReadList(String fullName, int namePos, PojoFieldDescriptor fieldDescriptor, List list, Class<?> valueClass, PojoReadCursor parentCursor) {
        AbstractCursor cursor = cursorsContainer.ensureCursor(fieldDescriptor, list.size(), parentCursor);
        if (!cursor.hasCurrent()) {
            String message = "List element " + (list.size() - 1) + " < [" + cursor.getSize() + "] for field " + fieldDescriptor + "!";
            statusesContainer.addStatus(ReadStatusEnum.LIST_READ_FAILED, message);
            return null;
        }
        int pos = cursor.getCurrentPosition();
        Object listElem = list.get(pos);
        return readObject(listElem, fullName, namePos + 1, valueClass, parentCursor);
    }

    private Object tryReadMap(Map<String, Object> obj, String fullName, int namePos, PojoFieldDescriptor fieldDescriptor, Class<?> valueClass, PojoReadCursor parentCursor) {
        Object obj2 = obj.get(fieldDescriptor.getSimpleName());
        return readObject(obj2, fullName, namePos + 1, valueClass, parentCursor);
    }

    private Object tryReadReflective(Object obj, String fullName, int namePos, PojoFieldDescriptor fieldDescriptor, Class<?> valueClass, PojoReadCursor parentCursor) {
        Method getter = ReflectionUtils.findGetter(obj, fieldDescriptor.getSimpleName());
        Object value = ReflectionUtils.getValue(getter, obj);
        return readObject(value, fullName, namePos + 1, valueClass, parentCursor);
    }
}
