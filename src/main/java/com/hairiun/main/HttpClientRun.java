package com.hairiun.main;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.hirisun.check.util.HttpClientUtil;
import com.hirisun.check.util.SimpleMailSender;
import com.hirisun.mail.MailSenderInfo;

public class HttpClientRun {
	
	public static void main(String[] args) throws IOException {
		StringBuffer info = new StringBuffer();
		
		Properties properties = new Properties();

		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("interface.properties");
		
		properties.load(in);;
		
		Iterator<String> it=properties.stringPropertyNames().iterator();
        while(it.hasNext()){
        	
            String key=it.next();
            
            String url = properties.getProperty(key);
     		
     		HttpClientUtil clientUtil = new HttpClientUtil();
     		
     		String message = clientUtil.get(url);

     		String code = message.split(",")[0];
     		String infomation = message.split(",")[1];
     		if(!message.startsWith("SUCCESS")){	//	验证不通过
     			info.append(url).append(" 失败！状态信息是：").append(code).append(",原因是： ").append(infomation).append("\r\n");
     			System.out.println(info.toString());
     		}else{
     			info.append(url).append(" 成功！").append("\r\n");
     			System.out.println(info.toString());
     		}
        }
		
		if(info != null){
			//	发件人信息
			MailSenderInfo mailInfo = new MailSenderInfo();
			
			Properties prop = new Properties();
			
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("frommail.properties");
			
			prop.load(inputStream);
			
			Iterator<String> iterator = prop.stringPropertyNames().iterator();
			
			Map<String,String> item = new HashMap<String, String>();
			
			while(iterator.hasNext()){
				String key = iterator.next();
				item.put(key, prop.getProperty(key));
			}
			
			mailInfo.setMailServerHost(item.get("mailServerHost"));   
			mailInfo.setMailServerPort(item.get("mailServerPort")); 
			if(item.get("validate").equalsIgnoreCase("true")){
				mailInfo.setValidate(true);   
			}else
				mailInfo.setValidate(false);
			mailInfo.setUserName(item.get("userName"));   
			mailInfo.setPassword(item.get("password"));//TODO 您的邮箱密码   加密解密操作
			mailInfo.setFromAddress(item.get("fromAddress"));
			mailInfo.setSubject("校验信息"); 
			
			//	收件人信息
			Properties property = new Properties();
			
			InputStream input= Thread.currentThread().getContextClassLoader().getResourceAsStream("sendmails.properties");
			
			property.load(input);
			
			Iterator<String> itea = property.stringPropertyNames().iterator();
			
			StringBuffer receivers = new StringBuffer();
			
			while(itea.hasNext()){
				
				if(receivers.length() > 0)
					receivers.append(";").append(property.getProperty(itea.next()));
				else
					receivers.append(property.getProperty(itea.next()));
			}
			
			mailInfo.setToAddress(receivers.toString());
			
			mailInfo.setContent(info.toString());
			
			SimpleMailSender sms = new SimpleMailSender();  
			
		    sms.sendTextMail(mailInfo);     //发送文体格式  
		}
		
 
	}

	
	
}
