package com.domariev.emailsender.service.impl;

import com.domariev.emailsender.model.MailingBody;
import com.domariev.emailsender.model.NewsMailingRequest;
import com.domariev.emailsender.model.SearchParameter;
import com.domariev.emailsender.model.enums.ContentType;
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

    private static final String TOPIC_URL = "https://news.google.com/rss/headlines/section/topic/";
    private static final String KEYWORDS_URL = "https://news.google.com/rss/search?q=";
    private static final String LOCATION_URL = "https://news.google.com/rss/headlines/section/geo/";


    @Override
    public List<MailingBody> formBody(NewsMailingRequest newsMailingRequest) {
        List<SearchParameter> categories = newsMailingRequest.getSearchParameterList();
        return searchNews(categories, newsMailingRequest.getContentType());
    }

    private static List<MailingBody> searchNews(List<SearchParameter> categories, ContentType contentType) {
        List<MailingBody> mailingBodyList = new ArrayList<>();
        categories
                .forEach(searchParameter -> mailingBodyList.addAll(getNewsByPriority(searchParameter, contentType)));
        return mailingBodyList;
    }

    private static List<MailingBody> getNewsByPriority(SearchParameter searchParameter, ContentType contentType) {
        List<MailingBody> mailingBodyList = new ArrayList<>();
        URL url;
        String location;
        String language;
        try {
            if (searchParameter.getNewsLocation() != null) {
                location = searchParameter.getNewsLocation().toString();
                language = searchParameter.getNewsLocation().getCode();
            } else {
                location = null;    // default news location if not set value
                language = null;
            }
            if (contentType.name().equals("TOPIC")) {
                url = new URL(TOPIC_URL + searchParameter.getTopicName() +
                        "?hl=" + language + "-" + location + "&gl=" + location + "&ceid=" + location + ":" + language);
            } else if (contentType.name().equals("LOCATION")) {
                url = new URL(LOCATION_URL + searchParameter.getSearchQuery() +
                        "?hl=" + language + "-" + location + "&gl=" + location + "&ceid=" + location + ":" + language);
            } else {
                url = new URL(KEYWORDS_URL + searchParameter.getSearchQuery() +
                        "&hl=" + language + "-" + location + "&gl=" + location + "&ceid=" + location + ":" + language);
            }
            XmlReader reader = new XmlReader(url);
            SyndFeed feed = new SyndFeedInput().build(reader);
            for (Object o : feed.getEntries()) {
                if (mailingBodyList.size() == searchParameter.getMailingPriority().getValue()) {
                    break;
                }
                SyndEntry entry = (SyndEntry) o;
                MailingBody mailingBody = new MailingBody();
                mailingBody.setSearchParameter(searchParameter);
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
