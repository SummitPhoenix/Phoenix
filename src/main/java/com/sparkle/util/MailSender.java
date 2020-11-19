package com.sparkle.util;

import lombok.extern.slf4j.Slf4j;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * SendMail
 *
 * @author K1181378
 */
@Slf4j
public class MailSender {

    public static void sendMail(String title, String text, String[] addressList) {
        try {
            Properties prop = new Properties();
            prop.setProperty("mail.host", "smtp.163.com");
            prop.setProperty("mail.transport.protocol", "smtp");
            prop.setProperty("mail.smtp.auth", "true");
            //使用JavaMail发送邮件的5个步骤
            //1、创建session
            Session session = Session.getInstance(prop);
            //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
            session.setDebug(true);
            //2、通过session得到transport对象
            Transport ts = session.getTransport();
            //3、连上邮件服务器，需要发件人提供邮箱的用户名和密码进行验证
            ts.connect("smtp.163.com", "13407547940@163.com", "aptx4869");

            for (String address : addressList) {
                //4、创建邮件
                Message message = createTextMail(title, text, address, session);
                //5、发送邮件
                ts.sendMessage(message, message.getAllRecipients());
            }

            ts.close();
        } catch (MessagingException e) {
            log.error("Send Mail ERROR:", e);
        }
    }

    /**
     * 创建一封只包含文本的邮件
     */
    private static MimeMessage createTextMail(String title, String text, String address, Session session) throws MessagingException {
        //创建邮件对象
        MimeMessage message = new MimeMessage(session);
        //指明邮件的发件人
        message.setFrom(new InternetAddress("13407547940@163.com"));
        //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(address));
        //邮件的标题
        message.setSubject(title);
        //邮件的文本内容
        message.setContent(text, "text/html;charset=UTF-8");
        //返回创建好的邮件对象
        return message;
    }

}