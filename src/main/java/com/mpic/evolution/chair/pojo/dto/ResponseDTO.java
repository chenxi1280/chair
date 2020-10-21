package com.mpic.evolution.chair.pojo.dto;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * creator：cxd
 * date:2020/8/3
 * @author cxd
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO implements Serializable {
    /**
     * 返回的消息
     */
    private String msg;

    /**
     * 返回成功的消息
     */
    private static String successMsg = "success";

    /**
     * 返回错误的消息
     */
    private static String errorMsg = "error";
    /**
     * 返回的状态
     */
    private Object status = 200;
    /**
     * 返回的结果
     */
    private Boolean res = true;

    /**
     * 返回的错误码
     */
    private Integer errorCode;
    /**
     * 返回的数据
     */
    private Object data;

    /**
     * 返回为成功的构造函数
     *
     * @param msg
     * @param data
     */
    public ResponseDTO(String msg, Object data) {
        this.msg = msg;
        this.data = data;
    }

    /**
     * 构造失败的回调结果
     *
     * @param msg
     * @param data
     * @param errorCode
     * @param status
     */
    public ResponseDTO(String msg, Object data, Integer errorCode, Object status) {
        this.msg = msg;
        this.data = data;
        this.errorCode = errorCode;
        this.status = status;
        this.res = false;
    }

    // 定义了一个函数。泛型函数
    public <T> T getObject(Class<T> cls) {

        if (getData() != null) {
            return JSON.parseObject(JSON.toJSONString(getData()), cls);
        }
        return null;
    }

    /**
     * 调用成功的时候，返回成功的状态
     *
     * @param msg
     * @param data
     * @return
     */
    public static ResponseDTO ok(String msg, Object data) {
        return new ResponseDTO(msg, data);
    }


    /**
     * 调用成功的时候，返回成功的状态
     *
     * @return
     */
    public static ResponseDTO get(boolean res) {
        if (res) {
            return ResponseDTO.ok(successMsg);
        } else {
            return ResponseDTO.fail(errorMsg);
        }
    }
    /**
     * 调用成功的时候，返回成功的状态
     *
     * @return
     */
    public static ResponseDTO get(boolean res,Object data) {
        if (res) {
            return ResponseDTO.ok(successMsg,data);
        } else {
            return ResponseDTO.fail(errorMsg);
        }
    }

    public static ResponseDTO ok(String msg) {
        return new ResponseDTO(msg, null);
    }

    public static ResponseDTO ok() {
        return new ResponseDTO("操作成功", null);
    }

    /**
     * 	调用失败的时候，返回失败的状态
     *
     * @param msg
     * @param data
     * @return
     */
    public static ResponseDTO fail(String msg, Object data, Integer errorCode, Object status) {
        return new ResponseDTO(msg, data, errorCode, status);
    }

    /**
     * 调用失败的时候，返回失败的状态
     */
    public static ResponseDTO fail(String msg) {
        return new ResponseDTO(msg, null, null, 500);
    }
}
