package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONWriter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.alibaba.fastjson2.JSONWriter.Feature.WriteNulls;

final class FieldWriterCharMethod<T>
        extends FieldWriter<T> {
    FieldWriterCharMethod(
            String fieldName,
            int ordinal,
            long features,
            String format,
            String label,
            Field field,
            Method method,
            Class fieldClass
    ) {
        super(fieldName, ordinal, features, format, label, fieldClass, fieldClass, field, method);
    }

    @Override
    public Object getFieldValue(T object) {
        try {
            return method.invoke(object);
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            throw new JSONException("invoke getter method error, " + fieldName, e);
        }
    }

    @Override
    public void writeValue(JSONWriter jsonWriter, T object) {
        Character value = (Character) getFieldValue(object);

        if (value == null) {
            jsonWriter.writeNull();
            return;
        }

        jsonWriter.writeChar(value);
    }

    @Override
    public boolean write(JSONWriter jsonWriter, T object) {
        Character value = (Character) getFieldValue(object);

        if (value == null) {
            if (((jsonWriter.context.getFeatures() | this.features) & WriteNulls.mask) != 0) {
                writeFieldName(jsonWriter);
                jsonWriter.writeNull();
                return true;
            }
            return false;
        }

        writeFieldName(jsonWriter);
        jsonWriter.writeChar(value);
        return true;
    }
}
