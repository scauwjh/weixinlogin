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
			String sendMessagePage = WeixinUtil.SEND_MESSAGE_PAGE.replace("[TOKEN]", this.token)
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
	
	
	public String sendMessageToAll(String content, Integer messageType) {
		try {
			String url = WeixinUtil.MASS_MESSAGE;
			StringBuilder req = new StringBuilder();
			req.append("type=").append(messageType)
				.append("&groupid=-1")
				.append("&token=").append(this.token)
				.append("&lang=zh_CN&random=&f=json&ajax=1&t=ajax-response");
			// send content
			if(messageType.equals(Constant.TEXT_TYPE))
					req.append("&content=").append(content);
			else if(messageType.equals(Constant.IMAGE_TEXT_TYPE)) {
				req.append("&app_id=").append(content)
					.append("&appmsgid=").append(content);
			}
			if (!this.httpsRequest(url, req.toString(), POST,
					url, TIMEOUT)) {
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
	public String bindServer(String url) {
		JSONObject ret = new JSONObject();
		try {
			// switch off the edit model
			String req = "flag=0&type=1&token=" + this.token;
			if (!this.httpsRequest(WeixinUtil.SWITCH_MODEL, req, POST,
					WeixinUtil.SWITCH_MODEL , TIMEOUT)) {
				System.out.println("request failed");
				ret.put("ret", "-1");
				ret.put("result", "failed to request: edit model");
				return ret.toString();
			}
			String content = this.dealConnection();
			System.out.println(content);
			JSONObject json = JSONObject.fromObject(content);
			if (!json.getJSONObject("base_resp").get("ret").equals(0)) {
				json.put("result", "failed to switch off edit model");
				return json.toString();
			}
			// switch on the develop model
			req = "flag=1&type=2&token=" + this.token;
			if (!this.httpsRequest(WeixinUtil.SWITCH_MODEL, req, POST,
					WeixinUtil.SWITCH_MODEL , TIMEOUT)){
				System.out.println("request failed");
				ret.put("ret", "-1");
				ret.put("result", "failed to request: develop model");
				return ret.toString();
			}
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
			if(!this.httpsRequest(reqUrl, req, POST,
					reqUrl, TIMEOUT)) {
				System.out.println("request failed");
				ret.put("ret", "-1");
				ret.put("result", "failed to request: bind server");
				return ret.toString();
			}
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
			ret.put("ret", "-1");
			ret.put("msg", "error");
			return ret.toString();
		}
	}
	
	public String saveOrUpdateImageText(Long appMsgId, List<ImageText> list) {
		String url = WeixinUtil.OPERATE_APPMSG;
		JSONObject ret = new JSONObject();
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
			this.httpsRequest(url, req.toString(), POST, url, TIMEOUT);
			JSONObject json = JSONObject.fromObject(this.dealConnection());
			System.out.println("req: " + req);
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR: 异常出错: create picture message");
			ret.put("ret", "-1");
			ret.put("msg", "error");
			return ret.toString();
		}
	}
	
	public String uploadImage() {
		try {
			this.getSources(Constant.IMAGE_TYPE, 0, 1);
			String url = WeixinUtil.FILE_UPLOAD;
			url = url.replace("[TOKEN]", this.token)
					.replace("[TICKET]", this.ticket)
					.replace("[TICKET_ID]", this.ticketId);
			String tmp = "action=upload_material&f=json&ticket_id=[TICKET_ID]&ticket=[TICKET]&token=[TOKEN]&lang=zh_CN";
			tmp = tmp.replace("[TOKEN]", this.token)
					.replace("[TICKET]", this.ticket)
					.replace("[TICKET_ID]", this.ticketId);
//			System.out.println(tmp);
			this.httpsRequest(url, tmp, "C:\\Users\\asus\\Pictures\\LifeFrame\\1.jpg",
					POST, url, TIMEOUT);
			String content = this.dealConnection();
			System.out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("upload image error");
		}
		return null;
	}
	
}
