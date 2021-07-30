package com.domariev.emailsender.controller;

import com.domariev.emailsender.model.NewsMailingRequest;
import com.domariev.emailsender.service.NewsMailingRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notify-me/request")
public class NewsMailingRequestController {

    private final NewsMailingRequestService newsMailingRequestService;

    @PostMapping("/subscribe")
    public ResponseEntity<NewsMailingRequest> mailingSubscribe
            (@RequestBody NewsMailingRequest newsMailingRequest) throws MessagingException {
        return ResponseEntity.ok(newsMailingRequestService.add(newsMailingRequest));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<NewsMailingRequest> getById(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(newsMailingRequestService.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<NewsMailingRequest>> getAllRequests() {
        return ResponseEntity.ok(newsMailingRequestService.findAll());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") String id) {
        newsMailingRequestService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);

    }
}
