package com.pl.adblocker.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.util.Log;


//因为AdBlockPlus内置的更新规则库的功能经常无法更新，所以实现了这个下载规则库的类，方便更新规则
//如果能直接使用内置的更新功能，则会更方便更新，请自测选择如何使用
public class ADBlockerPatterns{
	private static final String PATTERN="\\[.*?";
	private static final String CHARSET="UTF-8";
	
	public boolean convertFile(String srcPath,String desPath) throws IOException{
		String str=null;
		boolean addedHeader=false;
        Log.d("AdBlockNew","convert start!");
		File srcFile=new File(srcPath);
		File desFile=new File(desPath);
		BufferedWriter fw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(desFile), CHARSET));
		BufferedReader fr=new BufferedReader(new InputStreamReader(new FileInputStream(srcFile),CHARSET));
		while ((str=fr.readLine())!=null) {
			String des=convertPattern(str);
			if (!addedHeader&&des.contains("[Adblock Plus")) {
				addedHeader=true;
				des=getHeader();
			}
			fw.write(des);
			fw.write("\n");		
		}
		fw.flush();
		fw.close();
		fr.close();
        Log.d("AdBlockNew","convert over!");
		return true;
	}
	
	private String getHeader(){
		StringBuilder sb=new StringBuilder();
		sb.append("# Adblock Plus preferences\n");
		sb.append("version=4\n");
		sb.append("[Subscription]\n");
		sb.append("url=http://download.360.cn/easylistchina+easylist.txt\n");
		sb.append("title=EasyList China+EasyList\n");
		sb.append("fixedTitle=true\n");
		sb.append("homepage=http://abpchina.org/forum/\n");
		sb.append("lastDownload=").append(System.currentTimeMillis()/1000).append("\n");
		sb.append("downloadStatus=synchronize_ok\n");
		sb.append("lastSuccess=").append(System.currentTimeMillis()/1000).append("\n");
		sb.append("expires=").append(System.currentTimeMillis()/1000+10000000).append("\n");
		sb.append("softExpiration=").append(System.currentTimeMillis()/1000+10000000).append("\n");
		SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddHHmm");
		sb.append("version=").append(sf.format(new Date(System.currentTimeMillis()))).append("\n");
		sb.append("requiredVersion=2.0\n");
		sb.append("[Subscription filters]\n");
		return sb.toString();
	}
	
	private String convertPattern(String src){		
		StringBuilder des=new StringBuilder();
		Pattern pattern=Pattern.compile(PATTERN);
		Matcher matcher=pattern.matcher(src);
		if (!matcher.find()) {
			return src;
		}
		int start=matcher.start();
		int end=matcher.end();
		des.append(src.substring(0, start));
		des.append("\\");
		des.append(src.substring(start,end));
		if (end>=src.length()) {
			return des.toString();
		}		
		des.append(convertPattern(src.substring(end)));
		return des.toString();		
	}	
	
	public void getPatternsFile(String url,String srcPath) throws IOException{
        URL myURL = new URL(url);
        HttpURLConnection connection = null;
        Log.d("AdBlockNew","connecting to server!");
        
        if (myURL.getProtocol().toUpperCase().equals("HTTPS")) {
			trustAllHosts();
			HttpsURLConnection https = (HttpsURLConnection) myURL.openConnection();
//			https.setHostnameVerifier(HttpIgnoreSSL.DO_NOT_VERIFY);
			connection = https;
		} else {
			connection = (HttpURLConnection) myURL.openConnection();
		}
        connection.setRequestProperty("Content-type", "text/html");
        connection.setRequestProperty("Accept-Charset", CHARSET);
        connection.setRequestProperty("contentType", CHARSET);
        InputStream insr = connection.getInputStream();
        BufferedWriter fw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(srcPath),CHARSET ));
		BufferedReader fr=new BufferedReader(new InputStreamReader(insr,CHARSET));
        Log.d("AdBlockNew","download start!");
        String line=fr.readLine();
        while (line!=null) {
        	fw.write(line);
        	fw.write("\n");
        	line=fr.readLine();
        }
        fw.flush();
        fw.close();
        fr.close();
        Log.d("AdBlockNew","download over!");
	}
	
	public static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		// Android use X509 cert
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};
	
	public static void main(String args[]){		
	
		String srcPath=args[0];
		String desPath=args[1];
		String url=args[2];
		boolean isDownload=Boolean.parseBoolean(args[3]);
		try {
			ADBlockerPatterns adp=new ADBlockerPatterns();
			if (isDownload) {
				adp.getPatternsFile(url,srcPath);
			}
			adp.convertFile(srcPath, desPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}