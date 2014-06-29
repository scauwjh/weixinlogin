package com.weixin.login.api;

import java.util.List;

import com.weixin.login.core.beans.ImageText;

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
	 * 获取素材（Constant.IMAGE_TYPE）
	 * @param messageType
	 * @param begin
	 * @param count
	 * @return
	 */
	public String getSources(Integer messageType, Integer begin,
			Integer count);
	
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
	
	/**
	 * 获取用户信息
	 * @return
	 */
	public Boolean getAccountInformation();
	
	
	
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
	 * <p>群发接口</p>
	 * <p>每天限发一条</p>
	 * <p>如果提示需要绑定公众号，请提醒用户手动去微信公众平台绑定</p>
	 * @param content
	 * @param messageType
	 * @param groupId (-1 is all)
	 * @param sex (0 is all, 1 is male, 2 is female,
	 *  please use Constant.SEX_ALL etc)
	 * @return
	 */
	public String massSendMessage(String content, Integer messageType,
			Integer groupId, Integer sex);
	
	/**
	 * 绑定服务器
	 * @param url 自己服务器跟微信交互的接口
	 * @return token
	 */
	public String bindServer(String url);
	
	
	/**
	 * 更新或者修改图文
	 * @param appMsgId (null 为新建，否则为更新)
	 * @param list
	 * @return
	 */
	public String saveOrUpdateImageText(Long appMsgId, List<ImageText> list);
	
	/**
	 * 上传图片
	 * @param filePath
	 * @return
	 */
	public String uploadImage(String filePath);
	
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
	
	public String getOriginalId();
	
	public String getWeixinAccount();
	
	public String getWeixinName();
	
	public String getWeixinType();

	public String getCertification();
	
	public String getEmail();
	
}
