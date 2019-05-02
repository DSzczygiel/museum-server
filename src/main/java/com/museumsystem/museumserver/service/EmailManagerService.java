package com.museumsystem.museumserver.service;

import java.io.IOException;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.pdfbox.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.museumsystem.museumserver.config.ServerPaths;

@Service
public class EmailManagerService {

	@Value("${conf.ip}")
	private String serverIP;
	@Value("${server.port}")
	private String serverPort;
	
	private final String TITLE = "MuseumSystem";
	private final String ACTIVATION_URL = ServerPaths.SERVER_ACTIVATE_ACCOUNT_URL+"?token=";
    private final TemplateEngine templateEngine;
    private MessageSource messageSource;
    
    @Autowired
    private JavaMailSender javaMailSender;
    
    @Autowired
    public EmailManagerService(TemplateEngine templateEngine) {
    	this.templateEngine = templateEngine;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
    	this.messageSource = messageSource;
    }
    
    private String getLanguageFromTag(String str) {
        return str.length() < 2 ? str : str.substring(0, 2);
    }
    
    public void sendActivationEmail(String to, String userFirstName, String token, String lang) {
    	String mLang = getLanguageFromTag(lang);
    	String link = ACTIVATION_URL + token + "&lang=" + mLang;
    	
    	Context context = new Context();
    	//if(lang.contains(mLang)) {
    		context.setLocale(Locale.forLanguageTag(mLang));
    	//}else {
    		//context.setLocale(Locale.forLanguageTag("en"));
    	//}
        context.setVariable("name", userFirstName);
        context.setVariable("link", link);

        String body = templateEngine.process("activationemail.html", context);
        
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setSubject(TITLE);
            helper.setText(body, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        try{
        	javaMailSender.send(mail);
        }catch(MailAuthenticationException e) {
        	e.printStackTrace();
        }catch(MailSendException e) {
        	e.printStackTrace();
        }catch(MailException e) {
        	e.printStackTrace();
        }	
    }
    
    public void sendEmailWithTicket(String to, String attachmentFilename, InputStreamResource inputStreamResource) {
    	MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setSubject(TITLE);
            helper.addAttachment(attachmentFilename, new ByteArrayResource(IOUtils.toByteArray(inputStreamResource.getInputStream())), "application/pdf");
            helper.setText(" ", true);
            
            javaMailSender.send(mail);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
