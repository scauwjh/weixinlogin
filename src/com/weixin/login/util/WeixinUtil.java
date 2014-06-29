package com.weixin.login.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class WeixinUtil {
	
	public static final String LOGIN_URL = "https://mp.weixin.qq.com/cgi-bin/login?lang=zh_CN";
	public static final String VERIFY_CODE_URL = "https://mp.weixin.qq.com/cgi-bin/verifycode";
	// replace [TOKEN] [FAKEID]
	public static final String HEAD_IMG_URL = "https://mp.weixin.qq.com/misc/getheadimg?token=[TOKEN]&fakeid=[FAKEID]";
	// replace [TOKEN] [MSGID]（消息的id） [MODE]（large or small）
	public static final String RECEIVED_IMG_URL = "https://mp.weixin.qq.com/cgi-bin/getimgdata?token=[TOKEN]&msgid=[MSGID]&mode=[MODE]&source=&fileId=0&ow";
	// replace [TOKEN] [COUNT]（数量） [DAY]（时间范围，最近多少天）
	public static final String FANS_MESSAGE_URL = "https://mp.weixin.qq.com/cgi-bin/message?t=message/list&count=[COUNT]&day=[DAY]&token=[TOKEN]&lang=zh_CN";
	// replace [TOKEN] [LASTMSGID]
	public static final String NEW_MESSAGE_COUNT = "https://mp.weixin.qq.com/cgi-bin/getnewmsgnum?token=[TOKEN]&lastmsgid=[LASTMSGID]&t=ajax-getmsgnum&lang=zh_CN";
	// replace [TOKEN] [COUNT]
	public static final String FANS_URL = "https://mp.weixin.qq.com/cgi-bin/contactmanage?t=user/index&pagesize=[COUNT]&pageidx=0&type=0&token=[TOKEN]&lang=zh_CN";
	// post content
	public static final String SEND_MESSAGE_PAGE = "https://mp.weixin.qq.com/cgi-bin/singlesendpage?tofakeid=[FAKEID]&t=message/send&action=index&token=[TOKEN]&lang=zh_CN";
	public static final String SEND_MESSAGE = "https://mp.weixin.qq.com/cgi-bin/singlesend";
	//bind server
	public static final String SWITCH_MODEL = "https://mp.weixin.qq.com/misc/skeyform?form=advancedswitchform&lang=zh_CN";
	public static final String BIND_SERVER = "https://mp.weixin.qq.com/advanced/callbackprofile?t=ajax-response&token=[TOKEN]&lang=zh_CN";
	//mass message
	public static final String MASS_MESSAGE = "https://mp.weixin.qq.com/cgi-bin/masssend";
	// create pircutre message
	public static final String OPERATE_APPMSG = "https://mp.weixin.qq.com/cgi-bin/operate_appmsg";
	// 获取图片素材 replace [TOKEN] [COUNT] [BEGIN]
	public static final String IMAGE_SOURCES = "https://mp.weixin.qq.com/cgi-bin/filepage?type=2&begin=[BEGIN]&count=[COUNT]&t=media/list&token=[TOKEN]&lang=zh_CN";
	// 获取图文素材 replace [TOKEN] [COUNT] [BEGIN]
	public static final String IMAGE_TEXT_SOURCE = "https://mp.weixin.qq.com/cgi-bin/appmsg?begin=[BEGIN]&count=[COUNT]&t=media/appmsg_list&type=10&action=list&token=[TOKEN]&lang=zh_CN";
	// 文件上传 [TICKET_ID] [TICKET] [TOKEN]
	public static final String FILE_UPLOAD = "https://mp.weixin.qq.com/cgi-bin/filetransfer?action=upload_material&f=json&ticket_id=[TICKET_ID]&ticket=[TICKET]&token=[TOKEN]&lang=zh_CN";
	// 账户信息
	public static final String ACCOUNT_INFORMATION = "https://mp.weixin.qq.com/cgi-bin/settingpage?t=setting/index&action=index&token=[TOKEN]&lang=zh_CN";
	
	
	/**
	 * @param content
	 * @param beginStr
	 * @param endStr
	 * @param tag
	 * @param index 倒数多少个
	 * @return
	 */
	public static String weixinMessageRequest(String content, 
			String beginStr, String endStr, String tag, Integer index) {
		String message = null;
		try {
			// get the fans message which save in a script tag
			Document doc = Jsoup.parse(content);
			Elements elements = doc.getElementsByTag(tag);
			
			// return the script element
			message = elements.get(elements.size() - index).html();
			if (beginStr != null && endStr != null) {
				Integer a = message.indexOf(beginStr) + beginStr.length();
				Integer b = message.indexOf(endStr);
				message = message.substring(a, b);
			}
			return message;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("get message error");
			return null;
		}
	}
}
