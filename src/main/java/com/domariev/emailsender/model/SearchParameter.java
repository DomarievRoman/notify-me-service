package com.domariev.emailsender.model;

import com.domariev.emailsender.model.enums.TopicEnum;
import com.domariev.emailsender.model.enums.MailingPriority;
import com.domariev.emailsender.model.enums.NewsLocation;
import lombok.Data;

@Data
public class SearchParameter {

    private TopicEnum topicName;
    
    private String searchQuery;

    private int publishingDaysPeriod;

    private NewsLocation newsLocation;

    private MailingPriority mailingPriority;
}
