package org.zero.paymentservice.utils;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.zero.paymentservice.model.URLParameter;

import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@Component
public class URIDecoder {

    @SneakyThrows
    public static <T> T apply(String value, Class<T> objectClass) {
        var s = value.split("&");
        Map<String, String> list = new HashMap<>();
        for (var sd : s) {
            var field = sd.split("=");
            list.put(field[0], field[1]);
        }
        var constructor = objectClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        var object = constructor.newInstance();
        var fields = object.getClass().getDeclaredFields();
        for (var field : fields) {
            field.setAccessible(true);
            var fieldAnnotation = field.getAnnotation(URLParameter.class);
            if (fieldAnnotation != null && fieldAnnotation.ignore()) continue;
            var fieldName = field.getName();
            if (fieldAnnotation != null) {
                fieldName = fieldAnnotation.value();
            }
            var fieldValue = list.get(fieldName);
            if (fieldValue == null) continue;

            field.set(object, URLDecoder.decode(fieldValue));
        }
        return object;
    }
}
