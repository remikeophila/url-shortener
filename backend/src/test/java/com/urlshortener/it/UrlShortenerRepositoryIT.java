package com.urlshortener.it;

import com.urlshortener.model.ShortUrl;
import com.urlshortener.repository.UrlShortenerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class UrlShortenerRepositoryIT {

    @Autowired
    private UrlShortenerRepository urlShortenerRepository;

    @BeforeEach
    public void setup() {
        urlShortenerRepository.deleteAll();
        urlShortenerRepository.save(new ShortUrl("a137b375cc", "www.google.com"));
    }

    @AfterEach
    void tearDown() {
        urlShortenerRepository.deleteAll();
    }

    @Test
    void findById_withExistingId() {
        String destinationUrl = "www.google.com";
        String shortUrl = "a137b375cc";

        ShortUrl foundUrl = urlShortenerRepository.findById(shortUrl).orElse(null);
        assertThat(foundUrl).isNotNull();
        assertThat(foundUrl.getShortUrl()).isEqualTo(shortUrl);
        assertThat(foundUrl.getDestinationUrl()).isEqualTo(destinationUrl);
    }

    @Test
    void findById_withNonExistingId() {
        String shortUrl = "1234567890";

        ShortUrl foundUrl = urlShortenerRepository.findById(shortUrl).orElse(null);
        assertThat(foundUrl).isNull();
    }

    @Test
    void save_shouldBeSaved() {
        String destinationUrl = "www.notarius.com";
        String shortUrl = "0edcc98ee9";

        ShortUrl foundUrl = urlShortenerRepository.findById(shortUrl).orElse(null);
        assertThat(foundUrl).isNull();

        urlShortenerRepository.save(new ShortUrl(shortUrl, destinationUrl));

        foundUrl = urlShortenerRepository.findById(shortUrl).orElse(null);
        assertThat(foundUrl).isNotNull();
        assertThat(foundUrl.getShortUrl()).isEqualTo(shortUrl);
        assertThat(foundUrl.getDestinationUrl()).isEqualTo(destinationUrl);
    }

}
