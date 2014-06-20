package com.weixin.login.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import com.sonalb.net.http.cookie.Client;
import com.sonalb.net.http.cookie.CookieJar;
import com.weixin.login.util.TrustManager;

public class Base implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	protected static final String POST = "POST";
	protected static final String GET = "GET";
	protected static final String ENCODER = "UTF-8";
	protected static final Integer MESSAGE_TIMEOUT = 5000;
	protected static final Integer IMAGE_TIMEOUT = 10000;
	protected static final Integer TIMEOUT = 5000;
	
	protected Client client;
	protected CookieJar cookie;
	protected String token;
	protected HttpsURLConnection httpsUrlConn;
	protected String user;// 用户名
	protected String password;// 用户密码
	protected Integer errCode;// 登录返回码
	protected Boolean loginStatus;// 登录标记
	protected String lastMsgId;// 最后接收到的id
	
	public Base() {
		this.loginStatus = false;
	}
	
	
	/**
	 * https请求
	 * @param requestUrl
	 * @param requestStr
	 * @param method
	 * @return
	 */
	protected boolean httpsRequest(String requestUrl, String requestStr,
			String method, String referer, Integer timeOut) {
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			// 创建连接
			URL url = new URL(requestUrl);
			httpsUrlConn = (HttpsURLConnection) url.openConnection();
			httpsUrlConn.setSSLSocketFactory(ssf);
			// 设置输出、输入流、超时等
			httpsUrlConn.setDoOutput(true);
			httpsUrlConn.setDoInput(true);
			httpsUrlConn.setUseCaches(false);
			httpsUrlConn.setReadTimeout(timeOut);
			// set HTTP_REFERER（外连接设置）
			if (referer != null)
				httpsUrlConn.addRequestProperty("Referer", referer);
			httpsUrlConn.addRequestProperty("Host", "mp.weixin.qq.com");
			httpsUrlConn.addRequestProperty("Connection", "keep-alive");
			// 如果cookie不为空，则设置cookie
			if (cookie != null) {
				this.client.setCookies(httpsUrlConn, cookie);
			}
			// 设置请求方式
			httpsUrlConn.setRequestMethod(method);
			httpsUrlConn.connect();
			// 打印输出流
			if (requestStr != null) {
				OutputStream outputStream = httpsUrlConn.getOutputStream();
				outputStream.write(requestStr.getBytes(ENCODER));
				outputStream.flush();
				outputStream.close();
			}
			// 登录new client 获取 cookie
			if (client == null) {
				client = new Client();
			}
			cookie = client.getCookies(httpsUrlConn);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 处理连接
	 * @return String
	 * @throws IOException
	 */
	protected String dealConnection() {
		// 将返回的输入流转换成字符串
		try {
			InputStream inputStream = httpsUrlConn.getInputStream();
			StringBuffer sb = new StringBuffer();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, ENCODER);
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				sb.append(str + "\n");
			}
			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			httpsUrlConn.disconnect();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/* getter */
	public String getUser() { return this.user; }
	public void setUser(String user) { this.user = user; }
	
	public String getPassword() { return this.password; }
	public void setPassword(String password) { this.password = password; }
	
	public Integer getErrCode() { return errCode; }
	
	public Boolean getLoginStatus() { return loginStatus; }
	
	public String getLastMsgId() { return lastMsgId; }
	
	public String getToken() { return this.token; }

}
