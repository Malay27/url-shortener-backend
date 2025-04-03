package com.example.URL_Shortening_Service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "urls")
@EqualsAndHashCode(of = "shortUrl")  // Equality based on short URL
@ToString(exclude = "id")  // Avoids exposing ID in logs
public class UrlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 500)
    private String originalUrl;

    @Column(nullable = false, unique = true, length = 10)
    private String shortUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private int accessCount = 0;

    private LocalDateTime lastAccessed;
}
