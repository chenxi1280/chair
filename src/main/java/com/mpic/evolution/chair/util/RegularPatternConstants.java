package com.mpic.evolution.chair.util;

/**
 * 
 * 类名称：  RegularPatternConstants   
 * 描述：正则表达式  
 *@author  wusc   
 * 创建时间：  2018年11月26日 上午10:24:56 
 * 修改备注：
 *
 */
public class RegularPatternConstants {
    /**
     * 数字
     */
    public static final String REGULAR_DIGIST = "\\d+";

    /** 
     * 邮箱 */
    public static final String REGULAR_EMAIL = "\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+";

    /**
     * 小写字母
     */
    public static final String  REGULAR_LOWCASE_ALPHABET= "[a-z]+";

    /**
     * 大写字母
     */
    public static final String REGULAR_UPPERCASE_ALPHABET = "[A-Z]+";
    /**
     * IP地址
     */
    public static final String REGULAR_IP_ADDRESS = "([0-9a-fA-F]*:[0-9a-fA-F:.]*)|([\\d.]+)";

}
