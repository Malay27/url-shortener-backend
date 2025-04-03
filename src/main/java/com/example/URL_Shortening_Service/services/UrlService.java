package com.example.URL_Shortening_Service.services;

import com.example.URL_Shortening_Service.dto.UrlRequestDto;
import com.example.URL_Shortening_Service.dto.UrlResponseDto;

import java.util.Optional;

public interface UrlService {
    UrlResponseDto shortenUrl(UrlRequestDto urlRequestDto);

    Optional<UrlResponseDto> getOriginalUrl(String shortUrl);
}
