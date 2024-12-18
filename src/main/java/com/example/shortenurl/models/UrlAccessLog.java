package com.example.shortenurl.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class UrlAccessLog extends BaseModel{

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shortened_url_id", nullable = false)
    private ShortenedUrl shortenedUrl;
    private long accessedAt;
}
