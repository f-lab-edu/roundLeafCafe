package com.ypdchurch.roundleafcafe.order.enums;

public enum OrderStatus {
    ORDER_START("01"),
    ORDER_ACCEPTED("02"),
    ORDER_CONFIRMED("03"),
    COOKING("04"),
    MENU_ALREADY_COMPLETE("05"),
    PICK_UP_COMPLETE("06"),
    WHOLE_COMPLETE("07"),
    CANCEL("90");

    private String code;

    OrderStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
