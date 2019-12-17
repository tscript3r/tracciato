package pl.tscript3r.tracciato.infrastructure.validator;

import java.lang.reflect.Field;

interface FieldValueExtension {

    default Object getFieldValue(Object object, String fieldName) {
        try {
            Class<?> clazz = object.getClass();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
