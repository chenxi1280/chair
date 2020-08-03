package com.mpic.evolution.chair.common.returnvo;

/**
 *
 * 类名:
 * @author Xuezx
 * @date 2019/9/20 0020 16:36
 * 描述: 标准返回VO格式，其中data是泛型数据，可以塞各种vo
 */
public class ReturnVo<T> {

    private String flag;
    private String errCode;
    private String errMsg;
    private String extend;
    private T data;

    public String getFlag() {
        return flag;
    }
    public void setFlag(StatusEnum status) {
        this.flag = status.getValue();
    }
    public String getErrCode() {
        return errCode;
    }
    public void setErrCode(ErrorEnum error) {
        this.errCode = error.getValue();
        this.errMsg = error.getText();
    }
    public String getErrMsg() {
        return errMsg;
    }
    public String getExtend() {
        return extend;
    }
    public void setExtend(String extend) {
        this.extend = extend;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
	@Override
	public String toString() {
		return "ReturnVo [flag=" + flag + ", errCode=" + errCode + ", errMsg=" + errMsg + ", extend=" + extend
				+ ", data=" + data + "]";
	}


}