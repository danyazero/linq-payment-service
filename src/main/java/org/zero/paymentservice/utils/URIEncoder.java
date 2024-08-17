package org.zero.paymentservice.utils;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.zero.paymentservice.model.URLParameter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Component
public class URIEncoder {

    @SneakyThrows
    public static <T> Map<String, String> apply(T o) {
        var s = o.getClass().getDeclaredFields();
//        var list = new ArrayList<String>();
        Map<String, String> map= new HashMap<>();
        for (Field field : s) {
            field.setAccessible(true);
            var fieldAnnotation = field.getAnnotation(URLParameter.class);
            if (fieldAnnotation != null && fieldAnnotation.ignore()) continue;
            var fieldName = field.getName();
            if (fieldAnnotation != null) {
                fieldName = fieldAnnotation.value();
            }

            var value = field.get(o);

            if (value == null) continue;

            var encodedField = fieldName + "=" + value;
            map.put(fieldName, value.toString());
        }
        return map;
    }
}
