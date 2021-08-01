package com.domariev.emailsender.model;

import lombok.Data;

@Data
public class MailingBody {

    private SearchParameter searchParameter;

    private String title;

    private String newsLink;

}
