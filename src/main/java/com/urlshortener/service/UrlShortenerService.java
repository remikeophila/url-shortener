package com.urlshortener.service;

import com.urlshortener.exception.NoDestinationUrlException;
import com.urlshortener.model.ShortUrl;
import com.urlshortener.repository.UrlShortenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UrlShortenerService {
    private final UrlShortenerRepository urlShortenerRepository;

    @Autowired
    public UrlShortenerService(UrlShortenerRepository urlShortenerRepository) {
        this.urlShortenerRepository = urlShortenerRepository;
    }

    public String shorten(String destinationUrl) throws NoSuchAlgorithmException {
        Optional<ShortUrl> existingShortUrl = urlShortenerRepository.findByDestinationUrl(destinationUrl);
        if (existingShortUrl.isPresent()) {
            return existingShortUrl.get().getShortUrl();
        }

        String shortenUrl = generateShortUrl(destinationUrl);

        // Collision Management:
        // With current algorithm, if there are 1 200 000 unique shortened URLs in the database,
        // there would be a 50% chance a new URL will face a collision, so maybe is worth to manage the case
        int attempt = 0;
        Optional<ShortUrl> optionalShortUrl = urlShortenerRepository.findById(shortenUrl);
        while (optionalShortUrl.isPresent() && attempt < 10) {
            shortenUrl = generateShortUrl(destinationUrl, attempt);
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

    private static String generateShortUrl(String originalUrl) throws NoSuchAlgorithmException {
        return generateShortUrl(originalUrl, 0);
    }

    private static String generateShortUrl(String originalUrl, int attempt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.reset();
        digest.update((originalUrl + (attempt == 0? "": attempt)).getBytes());
        byte[] hash = digest.digest();
        BigInteger no = new BigInteger(1, hash);
        return no.toString(16).substring(0, 10);
    }

}
