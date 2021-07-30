package com.domariev.emailsender.service.impl;

import com.domariev.emailsender.model.MailingBody;
import com.domariev.emailsender.model.NewsMailingRequest;
import com.domariev.emailsender.service.EmailService;
import com.domariev.emailsender.service.MailingBodyService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    private final MailingBodyService mailingBodyService;

    @Override
    public MimeMessage sendEmail(NewsMailingRequest mailingRequest) throws MessagingException {
        List<MailingBody> newsBodyList = mailingBodyService.formBody(mailingRequest);
        String subjectCategories = getSelectedCategories(newsBodyList);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        String messageBody = formatEmailBody(newsBodyList);
        messageHelper.setTo(mailingRequest.getEmail());
        messageHelper.setSubject("Recent news by categories: " + subjectCategories);
        messageHelper.setText(messageBody, true);
        mailSender.send(message);
        return message;
    }


    private String getSelectedCategories(List<MailingBody> newsBodyList) {
        StringBuilder sb = new StringBuilder();
        List<String> categories = newsBodyList.stream()
                .map(n -> n.getCategory().getName().toString())
                .distinct()
                .collect(Collectors.toList());
        for (String category : categories) {
            sb.append(category.toLowerCase(Locale.ROOT)).append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    private String formatEmailBody(List<MailingBody> newsBodyList) {
        StringBuilder sb = new StringBuilder();
        Map<String, List<MailingBody>> collect = newsBodyList.stream()
                .collect(Collectors.groupingBy(c -> c.getCategory().getName().toString()));
        for (Map.Entry<String, List<MailingBody>> map : collect.entrySet()) {
            int count = 0;
            String category = map.getKey();
            String capitalizedCategory = category.substring(0, 1).toUpperCase() + category.substring(1).toLowerCase();
            sb.append("<p><h3>").append(capitalizedCategory).append(" Topic").append("</h3></p>");
            for (MailingBody body : map.getValue()) {
                sb.append("<h4>").append(++count).append(". ")
                        .append("<a href=\"").append(body.getNewsLink())
                        .append("\">").append(body.getTitle()).append("</a></h4>");
            }
        }
        return sb.toString();
    }
}
