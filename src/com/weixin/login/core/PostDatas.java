package com.weixin.login.core;

import java.util.List;

import net.sf.json.JSONObject;

import com.weixin.login.constant.Constant;
import com.weixin.login.core.beans.ImageText;
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
			String url = WeixinUtil.SEND_MESSAGE;
			String sendMessagePage = WeixinUtil.SEND_MESSAGE_PAGE
					.replace("[TOKEN]", this.token)
					.replace("[FAKEID]", fakeId);
			StringBuilder req = new StringBuilder();
			req.append("type=").append(messageType)
				.append("&tofakeid=").append(fakeId)
				.append("&imgcode=")
				.append("&token=").append(this.token)
				.append("&lang=zh_CN&random=&f=json&ajax=1&t=ajax-response");
			// send content
			if(messageType.equals(Constant.TEXT_TYPE))
					req.append("&content=").append(content);
			else if(messageType.equals(Constant.IMAGE_TEXT_TYPE)) {
				req.append("&app_id=").append(content)
					.append("&appmsgid=").append(content);
			}
			this.httpsRequest(url, req.toString(), POST, 
					sendMessagePage, TIMEOUT, true);
			return this.dealConnection();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("send message error");
			return createMsg("-1", "send to one error");
		}
	}
	
	
	public String massSendMessage(String content, Integer messageType, 
			Integer groupId, Integer sex) {
		try {
			String url = WeixinUtil.MASS_MESSAGE;
			StringBuilder req = new StringBuilder();
			req.append("type=").append(messageType)
				.append("&groupid=").append(groupId)
				.append("&sex=").append(sex)
				.append("&token=").append(this.token)
				.append("&lang=zh_CN&random=&f=json&ajax=1&t=ajax-response");
			// send content
			if(messageType.equals(Constant.TEXT_TYPE))
					req.append("&content=").append(content);
			else if(messageType.equals(Constant.IMAGE_TEXT_TYPE)) {
				req.append("&app_id=").append(content)
					.append("&appmsgid=").append(content);
			}
			this.httpsRequest(url, req.toString(), POST, url, TIMEOUT, true);
			return this.dealConnection();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("send message error");
			return createMsg("-1", "send to all error");
		}
	}
	
	/**
	 * 绑定服务器（开放接口）
	 * @param url
	 * @return
	 */
	public String bindServer(String url) {
		JSONObject ret = new JSONObject();
		try {
			// switch off the edit model
			String req = "flag=0&type=1&token=" + this.token;
			this.httpsRequest(WeixinUtil.SWITCH_MODEL, req, POST,
					WeixinUtil.SWITCH_MODEL , TIMEOUT, true);
			String content = this.dealConnection();
			System.out.println(content);
			JSONObject json = JSONObject.fromObject(content);
			if (!json.getJSONObject("base_resp").get("ret").equals(0)) {
				json.put("result", "failed to switch off edit model");
				return json.toString();
			}
			// switch on the develop model
			req = "flag=1&type=2&token=" + this.token;
			this.httpsRequest(WeixinUtil.SWITCH_MODEL, req, POST,
					WeixinUtil.SWITCH_MODEL , TIMEOUT, true);
			content = this.dealConnection();
			System.out.println(content);
			json = JSONObject.fromObject(content);
			if (!json.getJSONObject("base_resp").get("ret").equals(0)) {
				System.out.println("request failed");
				json.put("result", "failed to switch on develop model");
				return json.toString();
			}
			//bind server
			String reqUrl = WeixinUtil.BIND_SERVER.replace("[TOKEN]", this.token);
			String userToken = StringUtil.randString(6).toLowerCase();
			req = "url=" + url + "&callback_token=" + userToken;
			this.httpsRequest(reqUrl, req, POST, reqUrl, TIMEOUT, true);
			content = this.dealConnection();
			System.out.println(content);
			json = JSONObject.fromObject(content);
			if (!json.get("ret").equals("0")) {
				System.out.println("request failed");
				json.put("result", "failed to bind server");
				return json.toString();
			}
			ret.put("ret", "1");
			ret.put("token", userToken);
			return ret.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return createMsg("-1", "bind server error");
		}
	}
	
	public String saveOrUpdateImageText(Long appMsgId, List<ImageText> list) {
		String url = WeixinUtil.OPERATE_APPMSG;
		try {
			StringBuffer req = new StringBuffer("AppMsgId=");
			if (appMsgId != null) {
				req.append(appMsgId);
				req.append("&sub=").append("update");
			} else req.append("&sub=").append("create");
			req.append("&count=").append(list.size());
			int count = 0;
			for (ImageText it : list) {
				req.append("&title").append(count)
				.append("=").append(it.getTitle());
				req.append("&content").append(count)
				.append("=").append(it.getContent());
				req.append("&digest").append(count)
				.append("=").append(it.getDigest());
				req.append("&author").append(count)
				.append("=").append(it.getAuthor());
				req.append("&fileid").append(count)
				.append("=").append(it.getFileid());
				req.append("&show_cover_pic").append(count)
					.append("=").append(it.getShow_cover_pic());
				req.append("&sourceurl").append(count++)
				.append("=").append(it.getSourceurl());
			}
			req.append("&token=").append(this.token);
			req.append("&vid=&ajax=1&lang=zh_CN&f=json&t=ajax-response&random=0.6692259708600666");
			req.append("&type=").append(Constant.IMAGE_TEXT_TYPE);
			this.httpsRequest(url, req.toString(), POST, url, TIMEOUT, true);
			JSONObject json = JSONObject.fromObject(this.dealConnection());
//			System.out.println("req: " + req);
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("save or update image text error");
			return createMsg("-1", "save or update image text error");
		}
	}
	
	public String uploadImage(String filePath) {
		try {
			this.getSources(Constant.IMAGE_TYPE, 0, 1);
			String url = WeixinUtil.FILE_UPLOAD;
			url = url.replace("[TOKEN]", this.token)
					.replace("[TICKET]", this.ticket)
					.replace("[TICKET_ID]", this.ticketId);
			this.httpsRequest(url, null, filePath, POST, url, TIMEOUT, true);
			String content = this.dealConnection();
			System.out.println(content);
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("upload image error");
			return createMsg("-1", "upload image error");
		}
	}
	
}
