package com.urlshortener.service;

import com.urlshortener.service.generator.ShortUrlGenerator;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ShortUrlGeneratorTest {
    private final ShortUrlGenerator shortUrlGenerator = new ShortUrlGenerator();

    @Test
    public void generate_withSameCall_shouldReturnTheSameURL() throws NoSuchAlgorithmException {
        String destinationUrl = "www.google.com";
        String firstGenerated = shortUrlGenerator.generate(destinationUrl);
        assertThat(shortUrlGenerator.generate(destinationUrl)).isEqualTo(firstGenerated);
        assertThat(shortUrlGenerator.generate(destinationUrl)).isEqualTo(firstGenerated);
    }

    @Test
    public void generate_withDifferentAttempt_shouldReturnDifferentUrl() throws NoSuchAlgorithmException {
        String destinationUrl = "www.google.com";
        String firstGenerated = shortUrlGenerator.generate(destinationUrl);
        String secondGenerated = shortUrlGenerator.generate("www.google.com", 1);
        String thirdGenerated = shortUrlGenerator.generate("www.google.com", 2);

        assertThat(firstGenerated).isNotEqualTo(secondGenerated).isNotEqualTo(thirdGenerated);
        assertThat(secondGenerated).isNotEqualTo(firstGenerated).isNotEqualTo(thirdGenerated);
        assertThat(thirdGenerated).isNotEqualTo(firstGenerated).isNotEqualTo(secondGenerated);
    }
}
