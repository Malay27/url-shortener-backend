package com.example.URL_Shortening_Service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class UrlRequestDto {

    @NotBlank(message = "URL can't be blank")
    @Size(max = 500, message = "URL length must be within 500 characters")
    @URL(message = "Invalid URL format")
    private final String originalUrl;
}
