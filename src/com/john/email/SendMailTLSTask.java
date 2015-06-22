package com.john.email;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.os.AsyncTask;

public class SendMailTLSTask extends AsyncTask<String, Void, String> {

	private static String HOST = "smtp.gmail.com";
    @Override
    protected void onPreExecute() {
        //disableUI();
    }

    @Override
    protected String doInBackground(String... formFieldValues) {
    	final String username = formFieldValues[0];
		final String password = formFieldValues[1];
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
//		Session session = Session.getDefaultInstance( props, null );
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("jiaolong423@gmail.com","International Point of Sale"));// sender's email address
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(formFieldValues[2]));// 
			message.addRecipients(Message.RecipientType.CC, InternetAddress.parse("long@internationalpointofsale.com"));
			message.setReplyTo(InternetAddress.parse(username));
			message.setSubject(formFieldValues[3]);
			//message.setText(formFieldValues[4]); // for plain-text email
			message.setContent(formFieldValues[4], "text/html; charset=utf-8");  
 
            Transport transport = session.getTransport("smtp");
            transport.connect(HOST, username, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
 
			return "Mail sent to"+formFieldValues[2]+" successed!";
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Mail sent failed!";
    }

    @Override
    protected void onPostExecute(String result) {
    	
    }
}