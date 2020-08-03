package com.mpic.evolution.chair.common.returnvo;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 类名:
 * @author Xuezx
 * @date 2019/9/20 0020 16:35
 * 描述: 标准状态返回码
 */
public enum StatusEnum{

    /**
     *
     * 成功与失败
     */
    SUCCESS("1", "成功"),
    FAILURE("0", "失败");

    private String value;
    private String text;

    private StatusEnum(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public static Map<String, String> getMap() {
        Map<String, String> map = new HashMap<String, String>();
        StatusEnum[] systemArr = StatusEnum.values();
        for (StatusEnum system : systemArr) {
            map.put(String.valueOf(system.getValue()), system.getText());
        }

        return map;
    }

    public static String getText(String value) {
        StatusEnum[] systemArr = StatusEnum.values();
        for (StatusEnum system : systemArr) {
            if (value.equals(system.getValue())){
                return system.getText();
            }

        }

        return null;
    }

    public static StatusEnum getEnum(String value) {
        StatusEnum[] systemArr = StatusEnum.values();
        for (StatusEnum system : systemArr) {
            if (value.equals(system.getValue())){
                return system;
            }
        }

        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }


}
