package com.urlshortener.service;

import com.urlshortener.exception.NoDestinationUrlException;
import com.urlshortener.model.ShortUrl;
import com.urlshortener.repository.UrlShortenerRepository;
import com.urlshortener.service.generator.ShortUrlGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UrlShortenerServiceTest {

    @Mock
    private UrlShortenerRepository urlShortenerRepository;

    private UrlShortenerService urlShortenerService;

    @BeforeEach
    private void setup() {
        urlShortenerService = new UrlShortenerService(urlShortenerRepository, new ShortUrlGenerator());
    }

    @Test
    public void shorten_withExistingUrl_ShouldNotSave() throws NoSuchAlgorithmException {
        String destinationUrl = "www.google.com";
        String expectedShortUrl = "a137b375";
        when(urlShortenerRepository.findByDestinationUrl(destinationUrl))
                .thenReturn(Optional.of(new ShortUrl(expectedShortUrl, destinationUrl)));

        String result = urlShortenerService.shorten(destinationUrl);

        assertThat(result).isEqualTo(expectedShortUrl);
        verify(urlShortenerRepository).findByDestinationUrl(destinationUrl);
        verify(urlShortenerRepository, never()).save(any(ShortUrl.class));
    }

    @Test
    public void shorten_withNonExistingUrl_ShouldSave() throws NoSuchAlgorithmException {
        String destinationUrl = "www.google.com";
        String expectedShortUrl = "a137b375cc";

        when(urlShortenerRepository.findByDestinationUrl(destinationUrl))
                .thenReturn(Optional.empty());
        ArgumentCaptor<ShortUrl> shortUrlCaptor = ArgumentCaptor.forClass(ShortUrl.class);

        String result = urlShortenerService.shorten(destinationUrl);

        assertThat(result).isEqualTo(expectedShortUrl);
        verify(urlShortenerRepository).findByDestinationUrl(destinationUrl);
        verify(urlShortenerRepository).save(shortUrlCaptor.capture());
        ShortUrl capturedShortUrl = shortUrlCaptor.getValue();
        assertThat(capturedShortUrl.getShortUrl()).isEqualTo(expectedShortUrl);
        assertThat(capturedShortUrl.getDestinationUrl()).isEqualTo(destinationUrl);
        assertThat(result).isEqualTo(expectedShortUrl);
    }

    @Test
    public void shorten_withOneCollision_shouldGenerateTwice() throws NoSuchAlgorithmException {
        ShortUrlGenerator mockShortUrlGenerator = mock(ShortUrlGenerator.class);
        UrlShortenerService urlShortenerServiceUnderTest =
                new UrlShortenerService(urlShortenerRepository, mockShortUrlGenerator);
        String destinationUrl = "http://collide.com";
        String firstGeneratedUrl = "c0ll1d3hash";
        String secondGeneratedUrl = "un1qu3hash";
        when(mockShortUrlGenerator.generate(destinationUrl)).thenReturn(firstGeneratedUrl);
        when(mockShortUrlGenerator.generate(eq(destinationUrl), eq(0))).thenReturn(secondGeneratedUrl);
        when(urlShortenerRepository.findById(firstGeneratedUrl))
                .thenReturn(Optional.of(new ShortUrl(firstGeneratedUrl, "http://other-collide.com")));
        when(urlShortenerRepository.findById(secondGeneratedUrl)).thenReturn(Optional.empty());

        String result = urlShortenerServiceUnderTest.shorten(destinationUrl);

        assertThat(result).isEqualTo(secondGeneratedUrl);
        verify(mockShortUrlGenerator).generate(destinationUrl);
        verify(mockShortUrlGenerator).generate(destinationUrl, 0);
        verify(urlShortenerRepository).save(any(ShortUrl.class));
    }

    @Test
    public void shorten_withMoreThan10Collision_shouldThrowException() throws NoSuchAlgorithmException {
        ShortUrlGenerator mockShortUrlGenerator = mock(ShortUrlGenerator.class);
        UrlShortenerService urlShortenerServiceUnderTest =
                new UrlShortenerService(urlShortenerRepository, mockShortUrlGenerator);

        String destinationUrl = "http://always-collide.com";
        String collisionUrl = "c0ll1d3hash";
        when(mockShortUrlGenerator.generate(anyString())).thenReturn(collisionUrl);
        when(mockShortUrlGenerator.generate(eq(destinationUrl), anyInt())).thenReturn(collisionUrl);
        when(urlShortenerRepository.findById(collisionUrl))
                .thenReturn(Optional.of(new ShortUrl(collisionUrl, "http://other-collide.com")));

        Exception exception = assertThrows(RuntimeException.class, () -> urlShortenerServiceUnderTest.shorten(destinationUrl));

        String expectedMessage = "Impossible Short Generation";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(mockShortUrlGenerator, times(1)).generate(destinationUrl);
        verify(mockShortUrlGenerator, times(10)).generate(eq(destinationUrl), anyInt());
        verify(urlShortenerRepository, times(11)).findById(collisionUrl);
    }

    @Test
    void getUrl_withExistingUrl_shouldReturnDestinationUrl() {
        String shortUrl = "a137b375cc";
        String expectedDestinationUrl = "www.google.com";
        when(urlShortenerRepository.findById(shortUrl))
                .thenReturn(Optional.of(new ShortUrl(shortUrl, expectedDestinationUrl)));

        String result = urlShortenerService.getUrl(shortUrl);

        assertEquals(expectedDestinationUrl, result);
    }

    @Test
    public void getUrl_withNonExistingUrl_shouldThrowException() {
        String shortUrl = "perdu.com";
        when(urlShortenerRepository.findById(shortUrl)).thenReturn(Optional.empty());

        assertThrows(NoDestinationUrlException.class, () -> urlShortenerService.getUrl(shortUrl));
    }

}
