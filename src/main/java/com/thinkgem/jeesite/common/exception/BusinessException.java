package com.thinkgem.jeesite.common.exception;

import com.thinkgem.jeesite.common.enums.ExceptionEnum;

/**
 * @ClassName: ServiceException 
 * @Description: TODO
 * @author zhangxy
 * @date 2017年8月24日 上午9:37:42
 */
public class BusinessException extends RuntimeException{

	private static final long serialVersionUID = 4129756339384479097L;
	private Integer code;

    public BusinessException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMsg());
        this.code = exceptionEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
