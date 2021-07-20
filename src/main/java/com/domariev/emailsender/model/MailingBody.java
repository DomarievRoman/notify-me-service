package com.domariev.emailsender.model;

import lombok.Data;

@Data
public class MailingBody {

    private Category category;

    private String newsLink;

    private int amount;
}
