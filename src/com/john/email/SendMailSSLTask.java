package com.john.email;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.os.AsyncTask;

public class SendMailSSLTask extends AsyncTask<String, Void, String> {
	private static String HOST = "smtp.gmail.com";
	
    @Override
    protected void onPreExecute() {
        //disableUI();
    }

    @Override
    protected String doInBackground(final String... formFieldValues) {
    	final String USER = formFieldValues[0];
    	final String PASSWORD = formFieldValues[1];
    	Properties props = new Properties();
		props.put("mail.smtp.host", HOST);
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
 
		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(USER,PASSWORD);
				}
			});
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(formFieldValues[0]));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(formFieldValues[2]));
			message.setSubject(formFieldValues[3]);
			//message.setText(formFieldValues[4]);
			message.setContent(formFieldValues[4], "text/html; charset=utf-8");  
            //Use Transport to deliver the message
            Transport transport = session.getTransport("smtp");
            transport.connect(HOST, USER, PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
 
			return ("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
    }

    @Override
    protected void onPostExecute(String result) {
    	
    }
}