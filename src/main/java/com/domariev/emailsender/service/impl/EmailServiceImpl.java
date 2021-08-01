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
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        String messageBody = formatEmailBody(newsBodyList);
        messageHelper.setTo(mailingRequest.getEmail());
        if (mailingRequest.getContentType().name().equals("TOPIC")) {
            String subjectCategories = getSelectedCategories(newsBodyList);
            messageHelper.setSubject("Recent news by categories: " + subjectCategories);
        } else {
            messageHelper.setSubject("Recent news from Notify Me Service");
        }
        messageHelper.setText(messageBody, true);
        mailSender.send(message);
        return message;
    }


    private String getSelectedCategories(List<MailingBody> newsBodyList) {
        StringBuilder sb = new StringBuilder();
        List<String> categories = newsBodyList.stream()
                .map(n -> n.getSearchParameter().getTopicName().toString())
                .distinct()
                .collect(Collectors.toList());
        for (String category : categories) {
            sb.append(category.toLowerCase(Locale.ROOT)).append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    private String formatEmailBody(List<MailingBody> newsBodyList) {
        StringBuilder sb = new StringBuilder();
        Map<String, List<MailingBody>> newsByTopicMap;
        boolean isNone = newsBodyList.stream().anyMatch(n -> n.getSearchParameter().getTopicName().name().equals("NONE"));
        if (!isNone) {
            newsByTopicMap = newsBodyList.stream()
                    .collect(Collectors.groupingBy(c -> c.getSearchParameter().getTopicName().toString()));
        } else {
            newsByTopicMap = newsBodyList.stream()
                    .collect(Collectors.groupingBy(c -> c.getSearchParameter().getSearchQuery()));
        }
        for (Map.Entry<String, List<MailingBody>> map : newsByTopicMap.entrySet()) {
            int count = 0;
            String title = map.getKey();
            if (!isNone) {
                String capitalizedCategory = title.substring(0, 1).toUpperCase() + title.substring(1).toLowerCase();
                sb.append("<p><h3>").append(capitalizedCategory).append(" Topic").append("</h3></p>");
            } else {
                sb.append("<p><h3>").append("News by keywords: ").append(title).append("</h3></p>");
            }
            for (MailingBody body : map.getValue()) {
                sb.append("<h4>").append(++count).append(". ")
                        .append("<a href=\"").append(body.getNewsLink())
                        .append("\">").append(body.getTitle()).append("</a></h4>");
            }
        }
        return sb.toString();
    }
}
