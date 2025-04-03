package com.example.URL_Shortening_Service.services;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.example.URL_Shortening_Service.dto.UrlRequestDto;
import com.example.URL_Shortening_Service.dto.UrlResponseDto;
import com.example.URL_Shortening_Service.entity.UrlEntity;
import com.example.URL_Shortening_Service.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;

    private static final int SHORT_URL_LENGTH = 8;

    @Value("${BASE_URL}")
    private String baseUrl;

    /**
     * Shortens the given URL and saves it to the database.
     */
    @Transactional
    public UrlResponseDto shortenUrl(UrlRequestDto urlRequestDto) {
        String originalUrl = urlRequestDto.getOriginalUrl();
        System.out.println("originalUrl " + originalUrl);

        if (!isValidUrl(originalUrl)) {
            throw new IllegalArgumentException("Invalid URL format: " + originalUrl);
        }

        Optional<UrlEntity> existingUrl = urlRepository.findByOriginalUrl(originalUrl);
        if (existingUrl.isPresent()) {
            return mapToResponseDto(existingUrl.get());
        }

        String shortUrl = generateUniqueShortUrl();
        System.out.println("shortUrl " + shortUrl);

        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setShortUrl(shortUrl);
        urlEntity.setOriginalUrl(originalUrl);
        System.out.println("urlEntity " + urlEntity);

        urlRepository.save(urlEntity);

        return mapToResponseDto(urlEntity);
    }

    /**
     * Retrieves the original URL from a short URL.
     */
    public Optional<UrlResponseDto> getOriginalUrl(String shortUrl) {
        Optional<UrlEntity> urlEntityOpt = urlRepository.findByShortUrl(shortUrl);

        if (urlEntityOpt.isPresent()) {
            UrlEntity urlEntity = urlEntityOpt.get();
            urlEntity.setAccessCount(urlEntity.getAccessCount() + 1);
            urlEntity.setLastAccessed(LocalDateTime.now());
            urlRepository.save(urlEntity);

            return Optional.of(mapToResponseDto(urlEntity));
        }
        return Optional.empty();
    }

    private String generateUniqueShortUrl() {
        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            String shortUrl = NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, SHORT_URL_LENGTH);
            if (urlRepository.findByShortUrl(shortUrl).isEmpty()) {
                return shortUrl;
            }
        }
        throw new RuntimeException("Failed to generate a unique short URL after " + maxRetries + " attempts.");
    }

    private boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    /**
     * Converts an UrlEntity to UrlResponseDto.
     */
    private UrlResponseDto mapToResponseDto(UrlEntity urlEntity) {
        return new UrlResponseDto(urlEntity.getOriginalUrl(), urlEntity.getShortUrl(), urlEntity.getCreatedAt());
    }
}
