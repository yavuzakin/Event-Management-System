package yte.intern.project.common.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class SendEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmailWithAttachment(String to, String body, String subject, String imagePath) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        System.out.println("Sending email to " + to);
        mimeMessageHelper.setFrom("eventmanagement10line@gmail.com");
        mimeMessageHelper.setTo(to);
        //mimeMessageHelper.setText(body);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body + "<br/><br/><br/><img src='cid:qrcode'>", true);
        mimeMessageHelper.addInline("qrcode", new FileSystemResource(imagePath));

        javaMailSender.send(mimeMessage);
        System.out.println("Mail Send...");
    }

    /*javaMailSender.send(new MimeMessagePreparator() {
        public void prepare(MimeMessage mimeMessage) throws MessagingException {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom("me@mail.com");
            message.setTo("you@mail.com");
            message.setSubject("my subject");
            message.setText("my text <img src='cid:myLogo'>", true);
            message.addInline("myLogo", new ClassPathResource("img/mylogo.gif"));
            message.addAttachment("myDocument.pdf", new ClassPathResource("doc/myDocument.pdf"));
        }
    });*/
}
