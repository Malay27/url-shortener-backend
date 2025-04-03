package com.example.URL_Shortening_Service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UrlResponseDto {

    private final String originalUrl;

    private final String shortUrl;

    private final LocalDateTime createdAt;
}
