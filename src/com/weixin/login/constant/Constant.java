package com.weixin.login.constant;
/** 
 * @author wjh E-mail: 472174314@qq.com
 * @version 创建时间：2014年3月27日 下午8:03:36 
 * 
 *
 */
public class Constant {
	public static final String USER = "user"; // 微信公众号账户
	public static final String PASSWORD = "password"; // 密码
	public static final String ERROR = "error"; 
	public static final String ENCODER = "UTF-8"; // UTF-8编码
	public static final String HTML_TYPE = "text/html";
	public static final String JSON_TYPE = "application/json";
	
	public static final Integer I_ERROR = -1;
	public static final Integer I_SUCCESS = 1;
	public static final Integer I_FAILED = 0;
	public static final Integer I_UPDATE = 1; // 更新
	public static final Integer I_DO_NOTHING = 0; // 不做任何动作
	
	public static final Integer TEXT_TYPE = 1; // 文字
	public static final Integer IMAGE_TYPE = 2; // 图片
	public static final Integer IMAGE_TEXT_TYPE = 10; // 图文
	
	public static final Integer I_LOGIN_FAILED = -1; // 登录失败
	public static final Integer I_LOGIN_SUCCEED = 0; // 登录成功
	public static final Integer I_LOGIN_VERIFY = -8; // 需要验证码
	
	public static final String POST = "POST";
	public static final String GET = "GET";
	public static final Integer I_MESSAGE_TIMEOUT = 5000;
	public static final Integer I_IMAGE_TIMEOUT = 10000;
	public static final Integer I_TIMEOUT = 5000;
	
	public static final Integer GROUP_ALL = -1;
	
	public static final Integer SEX_ALL = 0;
	public static final Integer SEX_MALE = 1;
	public static final Integer SEX_FEMALE = 2;
}
