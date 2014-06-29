package com.weixin.login.core;

import java.util.Date;

import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.weixin.login.constant.Constant;
import com.weixin.login.util.WeixinUtil;

public class GetDatas extends Login {

	private static final long serialVersionUID = 1L;
	
	
	
	/**
	 * 获取验证码（开放接口）
	 * @param String savePath
	 */
	public Boolean getVerifyCode(String savePath) {
		try {
			// 获取 verifycode
			StringBuffer req = new StringBuffer();
			req.append("username=" + this.user).append(
					"&r=" + (new Date()).getTime());
			// 清空client，获取验证码图片，重新登录
			this.client = null;
			this.cookie = null;
			this.savePicture(WeixinUtil.VERIFY_CODE_URL, req.toString(), savePath);
			return true;
		} catch (Exception e) {
			System.out.println("get verifyCode error");
			return false;
		}
	}
	
	/**
	 * 获取信息（开放接口）
	 * @param count
	 * @param day
	 * @return 
	 * get the message content which save in 
	 * a object in the second script tag desc
	 */
	public String getMessage(Integer count, Integer day) {
		try {
			String url = WeixinUtil.FANS_MESSAGE_URL.replace("[TOKEN]", this.token);
			url = url.replace("[COUNT]", count.toString());
			url = url.replace("[DAY]", day.toString());
			if (!this.httpsRequest(url, GET, url, MESSAGE_TIMEOUT, true)) {
				System.out.println("request failed");
				return null;
			}
			String html = this.dealConnection();
			// get the fans message which save in a script tag
			Document doc = Jsoup.parse(html);
			Elements elements = doc.getElementsByTag("script");
			String rm = "seajs.use('message/list', wx_main);;";
			String scriptElement = elements.get(elements.size() - 2).toString();
			// get the lastMsgId
			Integer begin = scriptElement.indexOf("latest_msg_id : '") + 17;
			Integer end = scriptElement.indexOf("',");
			this.lastMsgId = scriptElement.substring(begin, end);
			// return the script element
			String message = elements.get(elements.size() - 2).html()
					.replace(rm, "").replace("wx.cgiData =", "");
			
			return message;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("get fans message error");
			return createMsg("-1", "get fans message error");
		}
	}
	
	/**
	 * 获取素材（开放接口）
	 * @param count
	 * @return 
	 * get the message content which save in 
	 * a object in the second script tag desc
	 */
	public String getSources(Integer messageType, Integer begin,
			Integer count) {
		String url = null;
		JSONObject json = new JSONObject();
		try {
			if (messageType.equals(Constant.IMAGE_TYPE))
				url = WeixinUtil.IMAGE_SOURCES;
			else if (messageType.equals(Constant.IMAGE_TEXT_TYPE))
				url = WeixinUtil.IMAGE_TEXT_SOURCE;
			url = url.replace("[COUNT]", count.toString())
					.replace("[BEGIN]", begin.toString())
					.replace("[TOKEN]", this.token);
			if (!this.httpsRequest(url, GET, url, IMAGE_TIMEOUT, true)) {
				System.out.println("request failed");
				json.put("ret", "-1");
				json.put("msg", "request failed");
				return json.toString();
			}
			String content = this.dealConnection();
			// get the sources message which save in a script tag
			// get ticket and ticket_id
			String beginStr = "data:";
			String endStr = "nick_name";
			String message = WeixinUtil.weixinMessageRequest(content, 
					beginStr, endStr, "script", 9);
			message += "}";
			message = message.replace(".join(\"\")", "");
			JSONObject ticket = JSONObject.fromObject(message);
			this.ticket = ticket.getString("ticket");
			this.ticketId = ticket.getString("user_name");
			
			beginStr = "wx.cgiData = ";
			endStr = "};";
			message = WeixinUtil.weixinMessageRequest(content, 
					beginStr, endStr, "script", 2);
			message += "}";
			return message;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("get sources error");
			return createMsg("-1", "get sources error");
		}
	}
	
	/**
	 * 获取消息的图片（开放接口） （when type = 2 get recimg）
	 * @param msgId msgid equals id
	 * @param mode (small or large)
	 * @param savePath
	 * @return
	 */
	public Boolean getReceivedImage(String msgId, String mode, String savePath) {
		String url = WeixinUtil.RECEIVED_IMG_URL.replace("[TOKEN]", this.token);
		url = url.replace("[MSGID]", msgId);
		url = url.replace("[MODE]", mode);
		return this.savePicture(url, null, savePath);
	}
	
	/**
	 * 获取用户头像（开放接口）
	 * @param fakeId
	 * @return
	 */
	public Boolean getHeadImage(String fakeId, String savePath) {
		String url = WeixinUtil.HEAD_IMG_URL.replace("[TOKEN]", this.token);
		url = url.replace("[FAKEID]", fakeId);
		return this.savePicture(url, null, savePath);
	}
	
	/**
	 * 获取新消息的数量（开放接口）
	 * @param lastMsgId
	 * @return
	 */
	public Integer getNewMessageCount(String lastMsgId) {
		try {
			// get new message num
			String url = WeixinUtil.NEW_MESSAGE_COUNT;
			url = url.replace("[TOKEN]", this.token);
			url = url.replace("[LASTMSGID]", lastMsgId);
			if (!this.httpsRequest(url, GET, url, MESSAGE_TIMEOUT, true)) {
				System.out.println("failed");
				return -1;
			}
			String ret = this.dealConnection();

			JSONObject json = JSONObject.fromObject(ret);
			Integer newMsgCount = Integer.parseInt(json
					.getString("newTotalMsgCount"));
			return newMsgCount;
		} catch (Exception e) {
			System.out.println("get new message count error");
			return 0;
		}
	}
	
	/**
	 * 获取粉丝（开放接口）
	 * @param count
	 * @return 
	 * get the message content which save in 
	 * a object in the second script tag desc
	 */
	public String getFans(Integer count) {
		try {
			String url = WeixinUtil.FANS_URL.replace("[TOKEN]", this.token);
			url = url.replace("[COUNT]", count.toString());
			this.httpsRequest(url, GET, url, MESSAGE_TIMEOUT, false);
			String content = this.dealConnection();
			// get the fans message which save in a script tag
			String beginStr = "groupsList : (";
			String endStr = ").contacts";
			return WeixinUtil.weixinMessageRequest(content, 
					beginStr, endStr, "script", 2);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("get fans error");
			return null;
		}
	}

}
