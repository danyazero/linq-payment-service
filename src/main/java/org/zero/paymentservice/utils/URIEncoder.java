package org.zero.paymentservice.utils;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.zero.paymentservice.model.URLParameter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.function.Function;

@Component
public class URIEncoder {
    @SneakyThrows
    public static <T> String apply(T o) {
        var s = o.getClass().getDeclaredFields();
        var list = new ArrayList<String>();
        for (Field field : s) {
            field.setAccessible(true);
            var fieldAnnotation = field.getAnnotation(URLParameter.class);
            if (fieldAnnotation != null && fieldAnnotation.ignore()) continue;
            var fieldName = field.getName();
            if (fieldAnnotation != null) {
                fieldName = fieldAnnotation.value();
            }

            var value = field.get(o);
            System.out.println(value);
            if (value == null) continue;

            var encodedField = fieldName + "=" + value;
            list.add(encodedField);
        }
        return String.join("&", list);
    }
}
