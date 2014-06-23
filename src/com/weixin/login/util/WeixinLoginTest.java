package com.weixin.login.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.sf.json.JSONObject;

//import net.sf.json.JSONObject;

import com.weixin.login.api.WeixinLoginAPI;
import com.weixin.login.api.impl.WeixinLogin;
import com.weixin.login.constant.Constant;
import com.weixin.login.core.beans.ImageText;

public class WeixinLoginTest {
	
	private static WeixinLoginAPI login;
	
	/* main 测试方法 */
	public static void main(String[] args) {
		String verifyCodeImg = "D:\\verifyCodeImg.jpg";
		
		String user = "978861379@qq.com";
		String password = "20041123op.com";
//		user = "scau_network@126.com";
//		password = "1qazse432";
		
		Scanner input = new Scanner(System.in);
		
//		System.out.println("enter user: ");
//		user = input.next();
//		System.out.println("enter password: ");
//		password = input.next();	
		
		login = new WeixinLogin();
		login.login(user, MD5.getMD5(password));
		while (login.getErrCode() == -6) {
			login.getVerifyCode(verifyCodeImg);
			String verifyCode = input.next();
			if (verifyCode.equals("0")) {
				continue;
			}
			login.loginWithVerifyCode(verifyCode);
		}
		if (login.getErrCode() != 0) {
			input.close();
			return;
		}
		/*** to do some test ***/
//		sendMessageToAll(Constant.IMAGE_TEXT_TYPE, "201131004");
//		sendMessageToAll(Constant.TEXT_TYPE, "群发接口测试 ——此消息来自IDE，请勿回复");
//		uploadImage();
//		getImageSources(0, 9999999);
//		getImageTextSources(0, 9999999);
//		saveImageText();
		updateImageText(201131004L);
		
		input.close();
	}
	
	public static void uploadImage() {
		JSONObject json = JSONObject.fromObject(
				login.uploadImage("C:\\Users\\asus\\Pictures\\LifeFrame\\1.jpg")
		);
		System.out.println(json.get("content"));
	}
	
	public static void saveImageText() {
		Long appMsgId = null;
		List<ImageText> list = new ArrayList<ImageText>();
		for (int i = 0; i < 2; i++) {
			ImageText it = new ImageText();
			it.setAuthor("wjh.ide");
			it.setContent("<span style=\"color:red\">图文" + i + "</span>");
			it.setDigest("这是摘要");
			it.setFileid("201127195");
			it.setShow_cover_pic("1");
			it.setSourceurl("http://www.baidu.com");
			it.setTitle("测试图文——此图文来自IDE，莫回复。");
			list.add(it);
		}
		String message = login.saveOrUpdateImageText(appMsgId, list);
		System.out.println(message);
	}
	
	public static void updateImageText(Long appMsgId) {
		List<ImageText> list = new ArrayList<ImageText>();
		for (int i = 0; i < 2; i++) {
			ImageText it = new ImageText();
			it.setAuthor("wjh.ide");
			it.setContent("<span style=\"color:red\">图文" + i + "</span>");
			it.setDigest("这是摘要");
			it.setFileid("201127195");
			it.setShow_cover_pic("1");
			it.setSourceurl("");
			it.setTitle("测试图文——此图文来自IDE，莫回复。");
			list.add(it);
		}
		String message = login.saveOrUpdateImageText(appMsgId, list);
		System.out.println(message);
	}
	
	public static void bindServer() {
		String url = "http://weixin.uutime.cn/interface/communicate.action";
		System.out.println(login.bindServer(url).toString());
	}
	
	public static void sendMessageToAll(Integer type, String content) {
		String message = login.sendMessageToAll(content, type);
		System.out.println(message);
	}
	
	public static void sendMessageToSomeOne() {
		String fakeId = "324178320";
		String message = login.sendMessage(fakeId, "10000049", 
				Constant.IMAGE_TEXT_TYPE);
		System.out.println(message);
	}
	
	public static void getImageSources(Integer begin, Integer count) {
		String message = login.getSources(Constant.IMAGE_TYPE, begin, count);
		JSONObject json = JSONObject.fromObject(message);
		System.out.println(json);
		System.out.println(json.getJSONArray("file_item")
				.getJSONObject(0).get("name"));
		
	}
	
	public static void getImageTextSources(Integer begin, Integer count) {
		String message = login.getSources(Constant.IMAGE_TEXT_TYPE, begin, count);
		JSONObject json = JSONObject.fromObject(message);
		System.out.println(json);
		System.out.println(json.getJSONArray("item")
				.getJSONObject(0).get("title"));
	}
	
	public static void getNewMessage() {
		String retContent = login.getMessage(15, 7);
		System.out.println("received content:\n" + retContent);
	}
	
	public static void getNewMessageCount() {
		String lastMsgId = "200497833";
		Integer newMsgCount = login.getNewMessageCount(lastMsgId);
		System.out.println("new message count: " + newMsgCount);
		System.out.println("lastMsgId: " + login.getLastMsgId());
	}
	
	public static void getReceivedImage() {
		String picMsgId = "200588979";
		String mode = "large";
		String recImg = "D:\\recImg.jpg";
		Boolean flag = login.getReceivedImage(picMsgId, mode, recImg);
		if (flag)
			System.out.println("get received image: ok");
		else System.out.println("get received image: error");
	}
	
	public static void getHeadImage() {
		String fakeId = "324178320";
		String headImg = "D:\\headImg.jpg";
		Boolean flag = login.getHeadImage(fakeId, headImg);
		if (flag)
			System.out.println("get head image: ok");
		else System.out.println("get head image: error");
	}
}
