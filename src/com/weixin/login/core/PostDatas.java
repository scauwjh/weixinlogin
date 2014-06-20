package com.weixin.login.core;

import net.sf.json.JSONObject;

import com.weixin.login.constant.Constant;
import com.weixin.login.util.StringUtil;
import com.weixin.login.util.WeixinUtil;

public class PostDatas extends GetDatas {

	private static final long serialVersionUID = 1L;

	/**
	 * 发送消息（开放接口）
	 * @param fakeId
	 * @param content 如果是图文或者其他的话发送相关的appid
	 * @param messageType
	 * @return
	 */
	public String sendMessage(String fakeId, String content, Integer messageType) {
		try {
			String url = WeixinUtil.SEND_MESSAGE_URL;
			String sendMessagePage = WeixinUtil.SEND_MESSAGE_PAGE.replace("[TOKEN]", this.token)
								.replace("[FAKEID]", fakeId);
			StringBuilder req = new StringBuilder();
			req.append("type=").append(messageType)
				.append("&tofakeid=").append(fakeId)
				.append("&imgcode=")
				.append("&token=").append(this.token)
				.append("&lang=zh_CN&random=&f=json&ajax=1&t=ajax-response");
			// send content
			if(messageType.equals(Constant.I_MESSAGE_TYPE))
					req.append("&content=").append(content);
			else if(messageType.equals(Constant.I_MESSAGE_PICTURE_TYPE)) {
				req.append("&app_id=").append(content)
					.append("&appmsgid=").append(content);
			}
			if (!this.httpsRequest(url, req.toString(), POST,
					sendMessagePage, TIMEOUT)) {
				System.out.println("request failed");
				return null;
			}
			return this.dealConnection();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("send message error");
			return null;
		}
	}
	
	/**
	 * 绑定服务器（开放接口）
	 * @param url
	 * @return
	 */
	public Boolean bindServer(String url) {
		// switch off the edit model
		String req = "flag=0&type=1&token=" + this.token;
		if (!this.httpsRequest(WeixinUtil.SWITCH_MODEL, req, POST,
				WeixinUtil.SWITCH_MODEL , TIMEOUT)) {
			System.out.println("request failed");
			return false;
		}
		String ret = this.dealConnection();
		System.out.println(ret);
		JSONObject json = JSONObject.fromObject(ret);
		if (!json.getJSONObject("base_resp").get("ret").equals(0)) {
			return false;
		}
		req = "flag=1&type=2&token=" + this.token;
		if (!this.httpsRequest(WeixinUtil.SWITCH_MODEL, req, POST,
				WeixinUtil.SWITCH_MODEL , TIMEOUT)){
			System.out.println("request failed");
			return false;
		}
		ret = this.dealConnection();
		System.out.println(ret);
		json = JSONObject.fromObject(ret);
		if (!json.getJSONObject("base_resp").get("ret").equals(0)) {
			return false;
		}
		//bind server
		String reqUrl = WeixinUtil.BIND_SERVER.replace("[TOKEN]", this.token);
		req = "url=" + url + "&callback_token=" + StringUtil.randString(6).toLowerCase();
		if(!this.httpsRequest(reqUrl, req, POST,
				reqUrl, TIMEOUT)) {
			System.out.println("request failed");
			return false;
		}
		ret = this.dealConnection();
		System.out.println(ret);
		json = JSONObject.fromObject(ret);
		if (!json.get("ret").equals("0")) {
			return false;
		}
		return true;
	}
	
}
