package com.urlshortener.service.generator;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class ShortUrlGenerator {
    public String generate(String originalUrl) throws NoSuchAlgorithmException {
        return generate(originalUrl, 0);
    }

    public String generate(String originalUrl, int attempt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.reset();
        digest.update((originalUrl + (attempt == 0? "": attempt)).getBytes());
        byte[] hash = digest.digest();
        BigInteger no = new BigInteger(1, hash);
        return no.toString(16).substring(0, 10);
    }
}
