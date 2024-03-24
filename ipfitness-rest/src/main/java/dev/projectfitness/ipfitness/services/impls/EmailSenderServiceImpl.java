package dev.projectfitness.ipfitness.services.impls;

import dev.projectfitness.ipfitness.exceptions.NotFoundException;
import dev.projectfitness.ipfitness.models.dtos.AttachmentEmail;
import dev.projectfitness.ipfitness.models.dtos.ProgramMailItem;
import dev.projectfitness.ipfitness.services.ActionLoggingService;
import dev.projectfitness.ipfitness.services.EmailSenderService;
import dev.projectfitness.ipfitness.util.ActionMessageResolver;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final ActionLoggingService actionLoggingService;

    @Value("${ipfitness.email.username}")
    private String senderUsername;
    @Value("${ipfitness.account.verify.base-url}")
    private String verificationBaseUrl;
    private static final String VERIFICATION_EMAIL_TEMPLATE = "verify-account-email-template";
    private static final String DAILY_NEW_PROGRAMS_EMAIL_TEMPLATE = "new-program-suggestions-email-template";

    @Override
    public void sendEmailWithAttachment(AttachmentEmail email) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(email.getSendTo());
            helper.setFrom(email.getEmailFrom());
            helper.setSubject(email.getSubject());
            helper.setText(email.getBody(), email.isHtml());
            if (email.getAttachmentFiles() != null) {
                for (int i = 0; i < email.getAttachmentFiles().size(); i++) {
                    MultipartFile file = email.getAttachmentFiles().get(i);
                    helper.addAttachment(file.getOriginalFilename() == null ? "file" + i : file.getOriginalFilename(), file);
                }
            }
        };

        try {
            mailSender.send(messagePreparator);
            actionLoggingService.logMessage(ActionMessageResolver.resolveAttachmentMailSend(email));
        } catch (MailException ex) {
            throw new NotFoundException();
        }
    }

    @Override
    public void sendVerificationEmail(String sendTo, String tokenId) {
        String subject = "IPFitness account verification";
        Context context = new Context();
        String verifyLink = verificationBaseUrl + "?token=" + tokenId;
        context.setVariable("verifyLink", verifyLink);
        String htmlContent = templateEngine.process(VERIFICATION_EMAIL_TEMPLATE, context);
        sendTemplatedHtmlMail(Collections.singletonList(sendTo), subject, htmlContent);
    }

    @Override
    public void sendFitnessProgramsSuggestions(List<ProgramMailItem> programs, List<String> sendTo, String categoryName) {
        String subject = "IPFitness - Today's created programs";
        Context context = new Context();
        context.setVariable("programs", programs);
        context.setVariable("category", categoryName);
        String htmlContent = templateEngine.process(DAILY_NEW_PROGRAMS_EMAIL_TEMPLATE, context);
        sendTemplatedHtmlMail(sendTo, subject, htmlContent);
    }

    private void sendTemplatedHtmlMail(List<String> sendTo, String subject, String htmlContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setTo(sendTo.toArray(String[]::new));
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
            actionLoggingService.logMessage(ActionMessageResolver.resolveTemplatedMailSend(sendTo, subject));
        } catch (MessagingException ex) {
            actionLoggingService.logException(ex);
        }
    }
}
