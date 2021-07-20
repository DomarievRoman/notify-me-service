package com.domariev.emailsender.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "mailingRequests")
public class NewsMailingRequest {

    @Id
    private String id;

    private String email;

    private List<Category> categoryList;

    private int timeToSendOut;
}
