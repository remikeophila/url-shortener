package com.urlshortener.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "shortUrls")
@Getter
@Setter
@AllArgsConstructor
public class ShortUrl {
    @Id
    private String shortUrl;
    private String destinationUrl;

}
