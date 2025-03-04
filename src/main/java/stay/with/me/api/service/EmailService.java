package stay.with.me.api.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.mapper.EmailCodeMapper;

import java.util.Random;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailCodeMapper emailCodeMapper;


    //비밀번호 분실 - 임시 비밀번호 발송
    public void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setFrom("sujinego3971@gmail.com");
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 발송 실패", e);
        }
    }

    //회원가입용 인증번호 전송
    public String sendVerificationCode(String email) {

        String code = String.format("%06d", new Random().nextInt(1000000));
        emailCodeMapper.saveCode(email, code);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("이메일 인증 코드");
        message.setText("인증 코드: " + code);
        mailSender.send(message);

        return code;
    }

    public boolean verifyCode(String email, String inputCode) {
        String storedCode = emailCodeMapper.getCode(email);
        if (storedCode != null && storedCode.equals(inputCode)) {
            emailCodeMapper.deleteCode(email);
            return true;
        }
        return false;
    }
}

