package com.mpic.evolution.chair.common.returnvo;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 类名:
 * @author Xuezx
 * @date 2019/9/20 0020 16:36
 * 描述: 程序标准错误码，不同的开头一般为不同的类别
 */
public enum ErrorEnum {
    //X开头的为通用制作视频错误码
    ERR_X00("X00", "userId不合法"),
    ERR_X01("X01", "作品id不合法"),
    //0开头为制作视频错误码

    ERR_000("000", "请求成功"),
    ERR_001("001", "用户还没有草稿！"),
    ERR_003("003", "userId不合法！" ),
    ERR_004("004", "删除草稿更新表失败"),
    ERR_005("005", "发布草稿更新表失败"),
    ERR_007("007", "存储视频失败！"),
    ERR_008("008", "没有查到相应的视频存储路径！"),
    ERR_009("009", "用户还没有草稿,并且创建默认草稿失败！"),
    ERR_010("010", "作品相关json参数转换不合法！"),
    ERR_011("011", "没有找到该作品"),
    //1开头为用户相关错误码
    ERR_100("100", "用户授权信息保存失败"),
    ERR_101("101", "获取openid失败"),
    ERR_102("102", "用户相关xxx"),

    //2开头为视频播放相关错误码
    ERR_200("200", "该视频不存在！"),
    ERR_201("201", "视频id不合法！"),
    ERR_202("202", "视频主被动历史记录点击flag不合法！"),
    ERR_203("203", "视频主被动历史记录播放时间不合法！"),

    //3开头为搜索相关错误码
    ERR_300("300", "搜索内容不合法！"),
    ERR_301("301", "搜索种类不合法！"),

    //4开头为评论相关错误码
    ERR_400("400", "评论页数不合法！"),
    ERR_401("401", "评论插入失败！"),

    //5开头为举报等相关
    ERR_500("500", "相同作品只能举报一次哦！"),


    //用户相关的
    ERR_603("603", "非法访问"),
    ERR_601("601", "无数据"),
    ERR_613("613", "已有3个工单处理，无法申请");

    private String value;
    private String text;

    private ErrorEnum(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public static Map<String, String> getMap() {
        Map<String, String> map = new HashMap<String, String>();
        ErrorEnum[] systemArr = ErrorEnum.values();
        for (ErrorEnum system : systemArr) {
            map.put(String.valueOf(system.getValue()), system.getText());
        }

        return map;
    }

    public static String getText(String value) {
        ErrorEnum[] systemArr = ErrorEnum.values();
        for (ErrorEnum system : systemArr) {
            if (value.equals(system.getValue())) {
                return system.getText();
            }
        }

        return null;
    }

    public static ErrorEnum getEnum(String value) {
        ErrorEnum[] systemArr = ErrorEnum.values();
        for (ErrorEnum system : systemArr) {
            if (value.equals(system.getValue())) {
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
