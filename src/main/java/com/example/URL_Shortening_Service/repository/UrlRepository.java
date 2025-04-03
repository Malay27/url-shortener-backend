package com.example.URL_Shortening_Service.repository;

import com.example.URL_Shortening_Service.entity.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlEntity, Long> {

    Optional<UrlEntity> findByShortUrl(String shortUrl);

    Optional<UrlEntity> findByOriginalUrl(String originalUrl);

    List<UrlEntity> findTop10ByOrderByAccessCountDesc();
}
