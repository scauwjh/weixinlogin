package com.weixin.login.util;

import java.util.Scanner;

import net.sf.json.JSONObject;

import com.weixin.login.api.WeixinLoginAPI;
import com.weixin.login.api.impl.WeixinLogin;

public class WeixinLoginTest {
	
	/* main 测试方法 */
	public static void main(String[] args) {
		String user = "978861379@qq.com";
		String password = "20041123op.com";
//		String user = "scau_network@126.com";
//		String pass = "1qazse432";
		String verifyCodeImg = "D:\\verifyCodeImg.jpg";
		
//		String fakeId = "324178320";
//		String picMsgId = "200588979";
//		String headImg = "D:\\headImg.jpg";
//		String recImg = "D:\\recImg.jpg";
//		String mode = "large";
//		String lastMsgId = "200497833";
//		String retContent = null;
//		String retUrl = null;
//		Integer newMsgCount = null;
		Scanner input = new Scanner(System.in);
		WeixinLoginAPI login = new WeixinLogin();
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
		String url = "http://weixin.uutime.cn/interface/communicate.action";
		if (login.bindServer(url)) {
			System.out.println("bind success!");
		} else System.out.println("bind error!");
		//send message
		//String message = login.sendMessage(fakeId, "10000049", Constants.MESSAGE_PICTURE_TYPE);
		//System.out.println(message);
		/*
		//get fans message
		String message = login.getSources(99999999);
		JSONObject json = JSONObject.fromObject(message);
		System.out.println(json);
		*/
		
		// get message
		String retContent = login.getMessage(15, 7);
		System.out.println("received content:\n" + retContent);
		
		/*
		// get new message count
		//newMsgCount = login.getNewMessageCount(lastMsgId);
		//System.out.println("new message count: " + newMsgCount);
		//System.out.println("lastMsgId: " + login.getLastMsgId());
		// get head image
		Boolean flag = login.getHeadImage(fakeId, headImg);
		if (flag)
			System.out.println("get head image: ok");
		else System.out.println("get head image: error");
		// get received image
		flag = login.getReceivedImage(picMsgId, mode, recImg);
		if (flag)
			System.out.println("get received image: ok");
		else System.out.println("get received image: error");
		*/
		input.close();
	}
}
