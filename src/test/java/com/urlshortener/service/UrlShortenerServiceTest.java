package com.urlshortener.service;

import com.urlshortener.exception.NoDestinationUrlException;
import com.urlshortener.model.ShortUrl;
import com.urlshortener.repository.UrlShortenerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
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

    @InjectMocks
    private UrlShortenerService urlShortenerService;

    @Test
    void shorten_withExistingUrl_ShouldNotSave() throws NoSuchAlgorithmException {
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
    void shorten_withNonExistingUrl_ShouldSave() throws NoSuchAlgorithmException {
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
    void getUrl_withExistingUrl_shouldReturnDestinationUrl() {
        String shortUrl = "a137b375cc";
        String expectedDestinationUrl = "www.google.com";
        when(urlShortenerRepository.findById(shortUrl))
                .thenReturn(Optional.of(new ShortUrl(shortUrl, expectedDestinationUrl)));

        String result = urlShortenerService.getUrl(shortUrl);

        assertEquals(expectedDestinationUrl, result);
    }

    @Test
    void getUrl_withNonExistingUrl_shouldThrowException() {
        String shortUrl = "perdu.com";
        when(urlShortenerRepository.findById(shortUrl)).thenReturn(Optional.empty());

        assertThrows(NoDestinationUrlException.class, () -> urlShortenerService.getUrl(shortUrl));
    }
}
