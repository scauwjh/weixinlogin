package com.weixin.login.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;


/** 
 * @author wjh E-mail: 472174314@qq.com
 * 说明：直接用java application运行就可以了
 */
public class ExecuteSQL {
	
	public ExecuteSQL() {}
	
	
	private static final String[] COMMENT = {"insert", "update", "delete"}; 
	private static final String BEGIN = "begin;";
	private static final String COMMIT = "commit;";
	private static final String ROLLBACK = "rollback;";
	private static Boolean transactionFlag;
	/**
	 * 执行sql
	 * @param path
	 * @param user
	 * @param password
	 * @throws Exception 
	 */
	public void exceSQLFile(String databaseUrl, String sqlFilePath, String user, String password) throws Exception{
		File sqlFile = new File(sqlFilePath);
		BufferedReader br = null;
		Connection con = null;
		Statement sta = null;
		try {
			String driver = "com.mysql.jdbc.Driver";
			Class.forName(driver);
			con = DriverManager.getConnection(databaseUrl, user, password);
			sta = con.createStatement();
			// 读取文件
			br = new BufferedReader(new FileReader(sqlFile));
			StringBuffer sb = new StringBuffer();
			String str = null;
			transactionFlag = false;
			while((str = br.readLine()) != null) {
				if(str.startsWith("#"))
					continue;
				sb.append(str);
				if (str.endsWith(";")) {
					String tmp = sb.toString();
					sb.delete(0, sb.length());
					int i = 0;
					for (i = 0; i < COMMENT.length; i++)
						if (tmp.toLowerCase().startsWith(COMMENT[i])) {
							if (!transactionFlag) {
								sta.execute(BEGIN);
								System.out.println("transaction begin");
							}
							transactionFlag = true;
							break;
						}
					if(i == COMMENT.length)
						transactionFlag = false;
					sta.execute(tmp);
					String print = "execute ok: ";
					print += tmp.length() > 50 ? tmp.substring(0, 50) + "..." : tmp;
					System.out.println(print);
				}
			}
			if (transactionFlag) {
				sta.execute(COMMIT);
				System.out.println("transaction commit");
			}
			sta.close();
			con.close();
			br.close();
		} catch (Exception e) {
			if (transactionFlag) {
				sta.execute(ROLLBACK);
				System.out.println("transaction rollback");
			}
			System.out.println("Execute failed!");
			throw e;
		}
	}
	
	public void execSQL(String sql, String databaseUrl, String user, String password) throws Exception {
		Connection con = null;
		Statement sta = null;
		try {
			String driver = "com.mysql.jdbc.Driver";
			Class.forName(driver);
			con = DriverManager.getConnection(databaseUrl, user, password);
			sta = con.createStatement();
			sta.execute(BEGIN);
			System.out.println("transaction begin");
			sta.execute(sql);
			System.out.println(sql.substring(0, 50));
			sta.execute(COMMIT);
			System.out.println("transaction commit");
			sta.close();
			con.close();
		} catch (Exception e) {
			sta.execute(ROLLBACK);
			System.out.println("transaction rollback");
			System.out.println("Execute failed!");
			throw e;
		}
	}
	
	public static void main(String[] args) {
		ExecuteSQL exec = new ExecuteSQL();
		Scanner scanner = new Scanner(System.in);
		System.out.println("输入数据库备份文件（*.sql）的绝对路径：");
		String path = scanner.next();
		// System.out.println("输入数据库帐号：");
		String user = "root";//scanner.next();
		// System.out.println("输入密码：");
		String password = "root";//scanner.next();
		String url = "jdbc:mysql://localhost:3306/weixinlogin";
		try {
			exec.exceSQLFile(url, path, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		scanner.close();
	}
}

