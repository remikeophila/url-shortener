package com.urlshortener.service;

import com.urlshortener.service.generator.ShortUrlGenerator;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ShortUrlGeneratorTest {
    private final ShortUrlGenerator shortUrlGenerator = new ShortUrlGenerator();

    @Test
    public void generate_shouldReturnCorrectShort() throws NoSuchAlgorithmException {
        assertThat(shortUrlGenerator.generate("www.google.com")).isEqualTo("a137b375cc");
    }

    @Test
    public void generate_withAttempt_shouldReturnCorrectShort() throws NoSuchAlgorithmException {
        assertThat(shortUrlGenerator.generate("www.google.com", 1)).isEqualTo("b86205f6c1");
        assertThat(shortUrlGenerator.generate("www.google.com", 2)).isEqualTo("d3f55abd68");
    }
}
