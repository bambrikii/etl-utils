package org.bambrikii.etl.model.transformer.adapters.java.utils;

import org.bambikii.etl.model.transformer.adapters.EtlRuntimeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {
    private ReflectionUtils() {
    }

    public static Method findSetter(Object obj, String propertyName, Class<?> valueCls) {
        String name = "set" + capitalizeName(propertyName);
        try {
            return obj.getClass().getMethod(name, valueCls);
        } catch (NoSuchMethodException ex) {
            throw new EtlRuntimeException("Failed to find method " + propertyName, ex);
        }
    }

    private static String capitalizeName(String propertyName) {
        return propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }

    public static Object setValue(Method setter, Object obj, Object value) {
        try {
            return setter.invoke(obj, value);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new EtlRuntimeException("Failed to invoke method " + setter.getName(), ex);
        }
    }

    public static Method findGetter(Object obj, String propertyName) {
        String name = "get" + capitalizeName(propertyName);
        try {
            return obj.getClass().getMethod(name);
        } catch (NoSuchMethodException ex) {
            throw new EtlRuntimeException("Failed to find method " + name, ex);
        }
    }

    public static Object getValue(Method method, Object obj) {
        try {
            return method.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new EtlRuntimeException("Failed to invoke method " + method.getName(), ex);
        }
    }

    public static Object tryNewInstance(Class<?> cls) {
        try {
            return cls.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new EtlRuntimeException("Failed to instantiate an object of [" + cls.getName() + "] class");
        }
    }
}
