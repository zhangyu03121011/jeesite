package com.thinkgem.jeesite.common.enums;

/**
 * @Description: 异常枚举类错误代码
 * @author Jacky
 * @date 2017年8月3日 下午7:13:48
 */
public enum ExceptionEnum {
	/** 成功 */
	success("成功",0),
	/** 系统内部异常 */
	system_error("系统内部异常",500),
	
	/** 业务逻辑异常 */
	business_exception("业务逻辑异常",501),
	
	/** 参数不能为空 */
	param_not_null("参数不能为空", 1001), 
	
	/** 未登录 */
	not_login("未登录", 1002), 
	
	/** 该用户已存在 */
	user_exist("该会员已存在", 1003),
	
	/** 该用户不存在 */
	user_not_exist("该会员不存在",1004),
	
	/** 参数验证失败 */
	bean_valid_exception("参数验证失败", 1005)
	;

	private String msg;
	private Integer code;

	private ExceptionEnum(String msg, Integer code) {
		this.msg = msg;
		this.code = code;
	}

	public static String getMsg(Integer code) {
		for (ExceptionEnum t : ExceptionEnum.values()) {
			if (t.getCode().equals(code)) {
				return t.msg;
			}
		}
		return null;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
