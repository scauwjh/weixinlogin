package com.weixin.login.core;

import net.sf.json.JSONObject;

import com.weixin.login.constant.Constant;
import com.weixin.login.util.WeixinUtil;

public class Login extends Base {

	private static final long serialVersionUID = 1L;

	/**
	 * 登录
	 * @param user
	 * @param password
	 */
	protected Integer login(String user, String password, String verifyCode) {
		System.out.println("is loading...");
		// 设置用户密码
		this.user = user;
		this.password = password;
		StringBuffer loginSb = new StringBuffer();
		loginSb.append("username=" + this.user)
				.append("&pwd=" + this.password).append("&imgcode=");
		if (verifyCode != null)
			loginSb.append(verifyCode);
		loginSb.append("&f=json");
		// 发出请求
		if (!this.httpsRequest(WeixinUtil.LOGIN_URL, loginSb.toString(), POST, WeixinUtil.LOGIN_URL,
				MESSAGE_TIMEOUT)) {
			System.out.println("登录失败");
			return -1;
		}
		// 请求成功
		String ret = this.dealConnection();
		JSONObject json = JSONObject.fromObject(ret);
		System.out.println(json);
		JSONObject tmpJson = json.getJSONObject("base_resp");
		this.errCode = (Integer) tmpJson.get("ret");
		if (errCode.equals(0)) {
			this.loginStatus = true;
			// 截取token
			String errMsg = json.getString("redirect_url");
			Integer b = errMsg.indexOf("&token=") + 7;
			Integer e = errMsg.length();
			this.token = errMsg.substring(b, e);
			return 0;
		} else if (errCode.equals(Constant.I_LOGIN_VERIFY)) {
			// 需要验证码
			System.out.println("需要要验证码");
		} else
			System.out.println(json);
		return errCode;
	}
	
	/**
	 * 登录接口
	 * @param user
	 * @param password
	 * @return
	 */
	public Integer login(String user, String password) {
		this.user = user;
		this.password = password;
		return this.login(user, password, null);
	}
	
	/**
	 * <p>登录接口</p>
	 * <p>登录失败返回-8时调用此接口</p>
	 * @param verifyCode
	 */
	public Integer loginWithVerifyCode(String verifyCode) {
		return this.login(this.user, this.password, verifyCode);
	}
	
	/**
	 * 登出接口
	 */
	public void logout() {
		this.loginStatus = false;
		this.cookie = null;
		this.client = null;
		this.httpsUrlConn = null;
	}
	

}
