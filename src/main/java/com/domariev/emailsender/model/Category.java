package com.domariev.emailsender.model;

import com.domariev.emailsender.model.enums.CategoryEnum;
import com.domariev.emailsender.model.enums.MailingPriority;
import lombok.Data;

@Data
public class Category {

    private CategoryEnum name;

    private MailingPriority mailingPriority;
}
