package top.aidan.community.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

/**
 * Created by Aidan on 2021/11/10 19:16
 * GitHub: github.com/huaxin0304
 * Blog: aidanblog.top
 */

@Component
public class MailClient {
    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);

    private final JavaMailSender mailSender;

    public MailClient(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String to, String subject, String content) {
        Session session;
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("发送右键失败：" + e.getMessage());
        }
    }
}
