package com.urlshortener.service;

import com.urlshortener.exception.NoDestinationUrlException;
import com.urlshortener.model.ShortUrl;
import com.urlshortener.repository.UrlShortenerRepository;
import com.urlshortener.service.generator.ShortUrlGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UrlShortenerService {
    private final UrlShortenerRepository urlShortenerRepository;
    private final ShortUrlGenerator shortUrlGenerator;

    @Autowired
    public UrlShortenerService(UrlShortenerRepository urlShortenerRepository, ShortUrlGenerator generateShortUrl) {
        this.urlShortenerRepository = urlShortenerRepository;
        this.shortUrlGenerator = generateShortUrl;
    }

    public String shorten(String destinationUrl) throws NoSuchAlgorithmException {
        Optional<ShortUrl> existingShortUrl = urlShortenerRepository.findByDestinationUrl(destinationUrl);
        if (existingShortUrl.isPresent()) {
            return existingShortUrl.get().getShortUrl();
        }

        String shortenUrl = shortUrlGenerator.generate(destinationUrl);

        // Collision Management:
        // With current algorithm, if there are 1 200 000 unique shortened URLs in the database,
        // there would be a 50% chance a new URL will face a collision, so maybe is worth to manage the case
        int attempt = 0;
        Optional<ShortUrl> optionalShortUrl = urlShortenerRepository.findById(shortenUrl);
        while (optionalShortUrl.isPresent() && attempt < 10) {
            shortenUrl = shortUrlGenerator.generate(destinationUrl, attempt);
            optionalShortUrl = urlShortenerRepository.findById(shortenUrl);
            attempt++;
        }

        // Very few chance that case happens after 10 attempts
        if (optionalShortUrl.isPresent()) {
            throw new RuntimeException("Impossible Short Generation");
        }

        ShortUrl shortUrl = new ShortUrl(shortenUrl, destinationUrl);
        urlShortenerRepository.save(shortUrl);
        return shortenUrl;
    }

    public String getUrl(String shortUrl) {
        return urlShortenerRepository.findById(shortUrl)
                .map(ShortUrl::getDestinationUrl)
                .orElseThrow(NoDestinationUrlException::new);
    }



}
