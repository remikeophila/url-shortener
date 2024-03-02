package com.urlshortener.controller;

import com.urlshortener.dto.DestinationUrlResponse;
import com.urlshortener.dto.ShortenRequest;
import com.urlshortener.dto.ShortenResponse;
import com.urlshortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService service;

    @PostMapping(value = "/shorten", produces = {"application/json"})
    public ResponseEntity<ShortenResponse> shorten(@RequestBody @Valid ShortenRequest shortenRequest) throws NoSuchAlgorithmException {
        String shortenedUrl = service.shorten(shortenRequest.getUrl());
        return ResponseEntity.ok(new ShortenResponse(shortenedUrl));
    }

    @GetMapping("/{shortenUrl}")
    public ResponseEntity<DestinationUrlResponse> getDestinationUrl(@PathVariable String shortenUrl) {
        String destinationUrl = service.getUrl(shortenUrl);
        return ResponseEntity.ok(new DestinationUrlResponse(destinationUrl));
    }
}
