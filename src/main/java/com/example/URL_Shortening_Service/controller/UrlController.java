package com.example.URL_Shortening_Service.controller;

import com.example.URL_Shortening_Service.dto.UrlRequestDto;
import com.example.URL_Shortening_Service.dto.UrlResponseDto;
import com.example.URL_Shortening_Service.services.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    /**
     * Handles the URL shortening request.
     */
    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@Valid @RequestBody UrlRequestDto urlRequestDto) {
        try {
            UrlResponseDto urlResponseDto = urlService.shortenUrl(urlRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(urlResponseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    /**
     * Redirects a short URL to the original long URL.
     */
    @GetMapping("/{shortUrl}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortUrl) {
        try {
            Optional<UrlResponseDto> urlResponseDto = urlService.getOriginalUrl(shortUrl);

            if (urlResponseDto.isPresent()) {
                String originalUrl = urlResponseDto.get().getOriginalUrl();
                return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Short URL not found");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
