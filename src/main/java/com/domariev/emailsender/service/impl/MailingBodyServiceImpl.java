package com.domariev.emailsender.service.impl;

import com.domariev.emailsender.model.Category;
import com.domariev.emailsender.model.MailingBody;
import com.domariev.emailsender.model.NewsMailingRequest;
import com.domariev.emailsender.service.MailingBodyService;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class MailingBodyServiceImpl implements MailingBodyService {

    @Override
    public List<MailingBody> formBody(NewsMailingRequest newsMailingRequest) {
        List<Category> categories = newsMailingRequest.getCategoryList();
        return searchNews(categories);
    }

    private static List<MailingBody> searchNews(List<Category> categories) {
        List<MailingBody> mailingBodyList = new ArrayList<>();
        categories
                .forEach(category -> mailingBodyList.addAll(getNewsByPriority(category)));
        return mailingBodyList;
    }

    private static List<MailingBody> getNewsByPriority(Category category) {
        List<MailingBody> mailingBodyList = new ArrayList<>();
        try {
            URL url = new URL("https://news.google.com/rss/headlines/section/topic/" + category.getName());
            XmlReader reader = new XmlReader(url);
            SyndFeed feed = new SyndFeedInput().build(reader);
            for (Object o : feed.getEntries()) {
                if (mailingBodyList.size() == category.getMailingPriority().value) {
                    break;
                }
                SyndEntry entry = (SyndEntry) o;
                MailingBody mailingBody = new MailingBody();
                mailingBody.setCategory(category);
                mailingBody.setTitle(entry.getTitle());
                mailingBody.setNewsLink(entry.getLink());
                mailingBodyList.add(mailingBody);
            }
        } catch (FeedException | IOException e) {
            e.printStackTrace();
        }
        return mailingBodyList;
    }
}
