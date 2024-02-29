package com.urlshortener.controller;

import com.urlshortener.dto.ShortenRequest;
import com.urlshortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/")
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService service;

    @PostMapping("/shorten")
    public ResponseEntity<String> shorten(@RequestBody ShortenRequest shortenRequest) throws NoSuchAlgorithmException {
        String shortenedUrl = service.shorten(shortenRequest.getUrl());
        return ResponseEntity.ok(shortenedUrl);
    }

    @GetMapping("/{shortenUrl}")
    public ResponseEntity<String> getDestinationUrl(@PathVariable String shortenUrl) {
        String originalUrl = service.getUrl(shortenUrl);
        return ResponseEntity.ok(originalUrl);
    }
}
