package yte.intern.project.common.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.internet.MimeMessage;

@Service
public class SendEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmailWithImage(String to, String body, String subject, String imagePath) throws Exception {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom("eventmanagement10line@gmail.com");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setText(body);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body + "<br/><br/><br/><img src='cid:qrcode'>", true);
        mimeMessageHelper.addInline("qrcode", new FileSystemResource(imagePath));

        System.out.println("Sending email to " + to);
        javaMailSender.send(mimeMessage);
        System.out.println("Mail Send...");
    }

}
