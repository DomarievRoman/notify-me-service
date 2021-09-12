package com.domariev.emailsender.service.impl;

import com.domariev.emailsender.model.MailingBody;
import com.domariev.emailsender.model.NewsMailingRequest;
import com.domariev.emailsender.service.EmailService;
import com.domariev.emailsender.service.MailingBodyService;
import lombok.RequiredArgsConstructor;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    private final MailingBodyService mailingBodyService;

    private static final String VELOCITY_TEMPLATE = "./src/main/resources/static/email-html.vm";

    @Override
    public MimeMessage sendEmail(NewsMailingRequest mailingRequest) throws MessagingException {
        List<MailingBody> newsBodyList = mailingBodyService.formBody(mailingRequest);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        StringWriter stringWriter = new StringWriter();
        fillVelocityTemplate(newsBodyList, stringWriter);
        messageHelper.setTo(mailingRequest.getEmail());
        if (mailingRequest.getContentType().name().equals("TOPIC")) {
            String subjectCategories = getSelectedCategories(newsBodyList);
            messageHelper.setSubject("Recent news by categories: " + subjectCategories);
        } else {
            messageHelper.setSubject("Recent news from Notify Me Service");
        }
        messageHelper.setText(stringWriter.toString(), true);
        mailSender.send(message);
        return message;
    }

    private List<Map<String, String>> formatEmailBody(List<MailingBody> newsBodyList) {
        List<Map<String, String>> velocityList = new ArrayList<>();
        Map<String, String> emailDetailsMap = new HashMap<>();
        Map<String, List<MailingBody>> newsByTopicMap;
        boolean isNone = newsBodyList.stream().anyMatch(n -> n.getSearchParameter().getTopicName().name().equals("NONE"));
        if (!isNone) {
            newsByTopicMap = newsBodyList.stream()
                    .collect(Collectors.groupingBy(c -> c.getSearchParameter().getTopicName().toString()));
        } else {
            newsByTopicMap = newsBodyList.stream()
                    .collect(Collectors.groupingBy(c -> c.getSearchParameter().getSearchQuery()));
        }
        for (Map.Entry<String, List<MailingBody>> map2 : newsByTopicMap.entrySet()) {
            emailDetailsMap.put("topic", map2.getKey());
            for (MailingBody body : map2.getValue()) {
                emailDetailsMap.put("title", body.getTitle());
                emailDetailsMap.put("newsLink", body.getNewsLink());
                emailDetailsMap.put("image", body.getImage());
                velocityList.add(emailDetailsMap);
                emailDetailsMap = new HashMap<>();
            }
        }
        return velocityList;
    }

    private void fillVelocityTemplate(List<MailingBody> newsBodyList, StringWriter stringWriter) {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init();
        VelocityContext velocityContext = new VelocityContext();
        List<Map<String, String>> velocityList = formatEmailBody(newsBodyList);
        velocityContext.put("emailList", velocityList);
        Template template = velocityEngine.getTemplate(VELOCITY_TEMPLATE);
        template.merge(velocityContext, stringWriter);
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
}
