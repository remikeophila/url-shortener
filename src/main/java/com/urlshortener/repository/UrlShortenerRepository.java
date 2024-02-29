package com.urlshortener.repository;

import com.urlshortener.model.ShortUrl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlShortenerRepository extends MongoRepository<ShortUrl, String> {
    Optional<ShortUrl> findByDestinationUrl(String destinationUrl);
}
