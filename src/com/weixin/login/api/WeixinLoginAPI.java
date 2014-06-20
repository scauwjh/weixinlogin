package com.weixin.login.api;

public interface WeixinLoginAPI {
	
	//-------------------------------------------
	//------------------- 登录  -------------------
	//-------------------------------------------
	/**
	 * 登录
	 * @param user
	 * @param pass
	 * @return
	 */
	public Integer login(String user, String pass);
	
	/**
	 * <p>如果登录失败，且提示需要验证码（-8）时</p>
	 * <p>调用这个方法重新登录</p>
	 * @param verifyCode
	 */
	public Integer loginWithVerifyCode(String verifyCode);
	
	/**
	 * 登出
	 */
	public void logout();
	
	
	//-------------------------------------------
	//------------------ 获取数据  ------------------
	//-------------------------------------------
	/**
	 * 获取验证码
	 * @param savePath 保存图片的路径
	 * @return
	 */
	public Boolean getVerifyCode(String savePath);
	
	/**
	 * 获取粉丝
	 * @param count 可以设置无限
	 * @return
	 */
	public String getFans(Integer count);
	
	/**
	 * 获取素材
	 * @param count 可以设置无限
	 * @return
	 */
	public String getSources(Integer count);
	
	/**
	 * 获取消息（文字，图文等等）
	 * @param count 数量
	 * @param day 多少天前
	 * @return
	 */
	public String getMessage(Integer count, Integer day);
	
	/**
	 * <p>获取最新消息的数量</p>
	 * <p>需要先调用getMessage</p>
	 * @param lastMsgId 最后收到的最后一条消息的id
	 * @return
	 */
	public Integer getNewMessageCount(String lastMsgId);
	
	/**
	 * 获取接收到的图片
	 * @param msgId 从获取的消息里面提取
	 * @param mode large or small
	 * @param savePath 保存路径
	 * @return
	 */
	public Boolean getReceivedImage(String msgId, String mode,
			String savePath);
	
	/**
	 * 获取用户头像
	 * @param fakeId 从消息中提取
	 * @param savePath
	 * @return
	 */
	public Boolean getHeadImage(String fakeId, String savePath);
	
	
	
	//-------------------------------------------
	//------------------ 发送数据  ------------------
	//-------------------------------------------
	/**
	 * <p>不限次数发送信息（不推荐使用）</p>
	 * <p>需要用户先发送消息，时效？小时</p>
	 * @param fakeId 从粉丝数据中获取
	 * @param content 对应图文的appid（appid从素材管理哪里获取）
	 * @param messageType 请查看Constant.java对应的类型（如：Constants.MESSAGE_TYPE）
	 * @return
	 */
	public String sendMessage(String fakeId, String content,
			Integer messageType);
	
	/**
	 * 绑定服务器
	 * @param url 自己服务器跟微信交互的接口
	 * @return
	 */
	public Boolean bindServer(String url);
	
	
	
	//-------------------------------------------
	//------------ getter or setter  ------------
	//-------------------------------------------
	public String getUser();
	public void setUser(String user);
	
	public String getPassword();
	public void setPassword(String password);
	
	public Integer getErrCode();
	
	public Boolean getLoginStatus();
	
	public String getLastMsgId();
	
	public String getToken();
	
}
