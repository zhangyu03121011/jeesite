package com.thinkgem.jeesite.common.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thinkgem.jeesite.common.enums.ExceptionEnum;
import com.thinkgem.jeesite.common.exception.ReturnMsg;
import com.thinkgem.jeesite.common.mapper.JsonMapper;

/**
 * @ClassName: ReturnMsgUtil 
 * @Description: http请求统一返回结果格式
 * @author zhangxy
 * @date 2017年5月8日 下午1:10:09
 */
public class ReturnMsgUtil {
	static Logger logger = LoggerFactory.getLogger(ReturnMsgUtil.class);
	public static ReturnMsg<Object> success(Object object) {
		ReturnMsg<Object> result = new ReturnMsg<Object>();
        result.setCode(ExceptionEnum.success.getCode());
        result.setMsg(ExceptionEnum.success.getMsg());
        result.setStatus(true);
        result.setData(object);
        logger.info("请求成功返回结果successResult={}",JsonMapper.toJsonString(result));
        return result;
    }

    public static ReturnMsg<Object> success() {
        return success(null);
    }

    public static ReturnMsg<Object> error(Integer code, String msg,String requestURL) {
    	ReturnMsg<Object> result = new ReturnMsg<Object>();
    	result.setStatus(false);
        result.setCode(code);
        result.setMsg(msg);
        result.setUrl(requestURL);
        logger.error("请求失败返回结果execptionResult={}",JsonMapper.toJsonString(result));
        return result;
    }
    
    public static ReturnMsg<Object> error(ExceptionEnum exceptionEnum) {
    	ReturnMsg<Object> result = new ReturnMsg<Object>();
    	result.setStatus(false);
        result.setCode(exceptionEnum.getCode());
        result.setMsg(exceptionEnum.getMsg());
        logger.error("请求失败返回结果execptionResult={}",JsonMapper.toJsonString(result));
        return result;
    }
}

