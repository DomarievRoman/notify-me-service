package com.domariev.emailsender.model;

import lombok.Data;

@Data
public class MailingBody {

    private Category category;

    private String title;

    private String newsLink;

}
