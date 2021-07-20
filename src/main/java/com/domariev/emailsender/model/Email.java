package com.domariev.emailsender.model;

import lombok.Data;

import java.util.List;

@Data
public class Email {

    private String recipient;

    private String subject;

    private List<MailingBody> String;
}
