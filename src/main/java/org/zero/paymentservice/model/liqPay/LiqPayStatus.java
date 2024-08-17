package org.zero.paymentservice.model.liqPay;

import java.util.Arrays;

public enum LiqPayStatus {
    HOLD_WAIT("Сума успішно заблокована на рахунку відправника", "hold_wait"),
    SUCCESS("Успішний платіж", "success"),


    ERROR("Некоректно заповнені дані", "error"),
    FAILURE("Неуспішний платіж", "failure"),
    REVERSED("Платіж повернений", "reversed");

    private String title;
    private String value;

    LiqPayStatus(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public static String getStatusByCode(String code) {
        var founded = Arrays.stream(LiqPayStatus.values()).filter(element -> element.value.equals(code)).findFirst();
        return founded.map(liqPayStatus -> liqPayStatus.value.toUpperCase()).orElse(null);
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }
}
