package com.domariev.emailsender.repository;

import com.domariev.emailsender.model.NewsMailingRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsMailingRequestRepository extends MongoRepository<NewsMailingRequest, String> {
}
