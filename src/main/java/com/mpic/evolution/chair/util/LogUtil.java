package com.mpic.evolution.chair.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil {

    private static Logger logger = LogManager.getLogger(LogUtil.class);

    private final static int MAX_LENGTH= 200;
    /**
      * 方法名:outputLog
      * @author Xuezx (◔‸◔）
     * @param interfaceName 接口名，mapping的地址
     * @param method post还是get
     * @param params 参数，将参数转为json字符串传入
     * @param resFlag 是否有结果返回，void方法此处为false
     * @param res 返回结果，转为Json字符串
     * @param remark 备注
      * @date 2020/8/21 9:11
      * 方法描述:  用于info日志打印
      */
    public static void outputLog(String interfaceName, String method, String params, Boolean resFlag, String res, String remark) {
        StringBuilder sb = new StringBuilder();
        if(params.length() > MAX_LENGTH){
            params = params.substring(0, 200);
        }
        if(res.length() > MAX_LENGTH){
            res = res.substring(0, 200);
        }
        sb.append("接口：").append(interfaceName).append(";方法：").append(method).append(";入参：").append(params);
        if (resFlag) {
            sb.append(";出参为：").append(res);
        } else {
            sb.append(";本接口未记录出参！");
        }
        sb.append("说明：").append(remark);
        logger.info(sb.toString());
    }

    /**
      * 方法名:outputError
      * @author Xuezx (◔‸◔）
      * @param interfaceName 接口名，mapping的地址
     * @param method post还是get
     * @param params 参数，将参数转为json字符串传入
     * @param resFlag 是否有结果返回，void方法此处为false
     * @param res 返回结果，转为Json字符串
     * @param remark 备注
     * @param e  错误
      * @date 2020/8/21 9:11
      * 方法描述:   用于异常打印
      */
    public static void outputError(String interfaceName, String method, String params, Boolean resFlag, String res,
                                   String remark,Exception e){
        StringBuilder sb = new StringBuilder();
        if(params.length() > MAX_LENGTH){
            params = params.substring(0, 200);
        }
        if(res.length() > MAX_LENGTH){
            res = res.substring(0, 200);
        }
            sb.append("接口：").append(interfaceName).append(";方法：").append(method).append(";入参：").append(params);
        if (resFlag) {
            sb.append(";出参为：").append(res);
        } else {
            sb.append(";本接口未记录出参！");
        }
        sb.append("说明：").append(remark);
        logger.error(sb.toString(), e);
    }

}
