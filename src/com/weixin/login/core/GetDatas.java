package com.weixin.login.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.imageio.ImageIO;

import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.weixin.login.util.WeixinUtil;

public class GetDatas extends Login {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 保存图片
	 * @param pictureUrl
	 * @param requestValue
	 * @param savePath
	 * @return
	 */
	protected boolean savePicture(String pictureUrl, String requestValue,
			String savePath) {
		if (!this.httpsRequest(pictureUrl, requestValue, POST, WeixinUtil.LOGIN_URL,
				IMAGE_TIMEOUT))
			return false;
		try {
			InputStream in = this.httpsUrlConn.getInputStream();
			savePath = savePath.replace("\\", "/");
			File file = new File(savePath);
			String check = savePath.substring(0, savePath.lastIndexOf("/"));
			File folder = new File(check);
			if (!folder.exists())
				folder.mkdirs();
			FileOutputStream fout = new FileOutputStream(file);
			BufferedImage image = ImageIO.read(in);
			ImageIO.write(image, "png", fout);
			fout.flush();
			fout.close();
			this.httpsUrlConn.disconnect();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
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
			if (!this.httpsRequest(url, null, GET, url, MESSAGE_TIMEOUT)) {
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
			return null;
		}
	}
	
	/**
	 * 获取素材（开放接口）
	 * @param count
	 * @return 
	 * get the message content which save in 
	 * a object in the second script tag desc
	 */
	public String getSources(Integer count) {
		try {
			String url = WeixinUtil.SOURCES_URL.replace("[TOKEN]", this.token);
			url = url.replace("[COUNT]", count.toString());
			if (!this.httpsRequest(url, null, GET, url, MESSAGE_TIMEOUT)) {
				System.out.println("request failed");
				return null;
			}
			String content = this.dealConnection();
			// get the fans message which save in a script tag
			String beginStr = "wx.cgiData = ";
			String endStr = ",\"is_upload_cdn";
			String message = WeixinUtil.weixinMessageRequest(content, 
					beginStr, endStr, "script", 2);
			message += "}";
			return message;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("get fans error");
			return null;
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
			if (!this.httpsRequest(url, null, GET, url, MESSAGE_TIMEOUT)) {
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
			if (!this.httpsRequest(url, null, GET, url, MESSAGE_TIMEOUT)) {
				System.out.println("request failed");
				return null;
			}
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
