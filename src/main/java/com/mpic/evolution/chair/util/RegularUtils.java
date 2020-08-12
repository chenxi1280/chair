package com.mpic.evolution.chair.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mpic.evolution.chair.common.constant.RegularPatternConstants;

/**
 * 
 * 类名称：  RegularUtils   
 * 描述：  正则工具
 *@author  wusc   
 * 创建时间：  2018年11月21日 下午2:47:58 
 * 修改备注：
 *
 */
public class RegularUtils {
 
    
    /**
     * 
      * 方法名：  isDigst 
      * 描述：  数字判断
     * @author  wusc   
      * 创建时间：2018年11月26日 上午10:30:27
     * @param cs
     * @return
     *
     */
    public static boolean isDigst(final CharSequence cs){
        Matcher m = Pattern.compile(RegularPatternConstants.REGULAR_DIGIST).matcher(cs);
        return m.matches();
    }
    /**
     * 
      * 方法名：  isNotDigst 
      * 描述：  不是纯数字
     * @author  wusc   
      * 创建时间：2018年11月26日 上午10:32:19
     * @param cs
     * @return
     *
     */
    public static boolean isNotDigst(final CharSequence cs){
        return  !isDigst(cs);
    }
    
    /**
     * 
      * 方法名：  isEmail 
      * 描述：  email 判断
     * @author  wusc   
      * 创建时间：2018年11月26日 上午10:41:42
     * @param email
     * @return
     *
     */
    public static boolean isEmail(String email){
        Matcher m = Pattern.compile(RegularPatternConstants.REGULAR_EMAIL).matcher(email);
        return m.matches();
    }
    /**
     * 
      * 方法名：  isNotEmail 
      * 描述：  不是email
     * @author  wusc   
      * 创建时间：2018年11月26日 上午10:46:37
     * @param email
     * @return
     *
     */
    public static boolean isNotEmail(String email){
        return !isEmail(email);
    }
}
