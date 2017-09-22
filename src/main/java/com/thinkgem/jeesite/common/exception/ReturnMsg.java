package com.thinkgem.jeesite.common.exception;
/**
 * @ClassName: Result 
 * @Description: http请求返回的最外层对象
 * @author zhangxy
 * @date 2017年8月24日 上午9:23:34
 */
public class ReturnMsg<T> {

    /** 错误码. */
    private Integer code;

    /** 提示信息. */
    private String msg;
    
    /** 请求URL */
    private String url;

    /** 具体的内容. */
    private T data;
    
    /** 返回状态 */
    private Boolean status;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
}
