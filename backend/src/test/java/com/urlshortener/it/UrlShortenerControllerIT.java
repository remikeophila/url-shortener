package com.urlshortener.it;


import com.urlshortener.service.UrlShortenerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UrlShortenerControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlShortenerService urlShortenerService;

    @Test
    public void whenShortenURL_ShouldParseAndReturnCorrectValue() throws Exception {
        String destinationUrl = "www.google.com";
        String shortUrl = "a137b375cc";

        given(urlShortenerService.shorten(destinationUrl)).willReturn(shortUrl);

        mockMvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\":\"" + destinationUrl + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"shortenedUrl\":\"" + shortUrl + "\"}"));
    }

    @Test
    public void whenGetURL_ShouldReturnTheCorrectURL() throws Exception {
        String destinationUrl = "www.google.com";
        String shortUrl = "a137b375cc";

        given(urlShortenerService.getUrl(shortUrl)).willReturn(destinationUrl);

        mockMvc.perform(get("/{shortUrl}", shortUrl))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"destinationUrl\":\"" + destinationUrl + "\"}"));
    }
}
