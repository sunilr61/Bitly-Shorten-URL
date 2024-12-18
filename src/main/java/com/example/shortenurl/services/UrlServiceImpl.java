package com.example.shortenurl.services;

import com.example.shortenurl.exceptions.UrlNotFoundException;
import com.example.shortenurl.exceptions.UserNotFoundException;
import com.example.shortenurl.models.ShortenedUrl;
import com.example.shortenurl.models.UrlAccessLog;
import com.example.shortenurl.models.User;
import com.example.shortenurl.repositories.ShortenedUrlRepository;
import com.example.shortenurl.repositories.UrlAccessLogRepository;
import com.example.shortenurl.repositories.UserRepository;
import com.example.shortenurl.utils.ShortUrlGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UrlServiceImpl implements UrlService{
    UserRepository userRepository;
    ShortenedUrlRepository shortenedUrlRepository;
    UrlAccessLogRepository urlAccessLogRepository;
    @Autowired
    public UrlServiceImpl(UserRepository userRepository,
                          ShortenedUrlRepository shortenedUrlRepository,
                          UrlAccessLogRepository urlAccessLogRepository){
        this.userRepository=userRepository;
        this.shortenedUrlRepository=shortenedUrlRepository;
        this.urlAccessLogRepository=urlAccessLogRepository;
    }
    @Override
    public ShortenedUrl shortenUrl(String url, int userId) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException("User is not valid");
        }
        User user = userOptional.get();
        int day = switch (user.getUserPlan()){
            case FREE -> 1;
            case TEAM -> 7;
            case BUSINESS -> 30;
            case ENTERPRISE -> 365;
        };
        long planTime = day * 24L * 60 * 60 * 1000;
        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setOriginalUrl(url);
        shortenedUrl.setExpiresAt(System.currentTimeMillis()+planTime);
        shortenedUrl.setUser(user);
        shortenedUrl.setShortUrl(ShortUrlGenerator.generateShortUrl());
        return shortenedUrlRepository.save(shortenedUrl);
    }

    @Override
    public String resolveShortenedUrl(String shortUrl) throws UrlNotFoundException {
        Optional<ShortenedUrl> shortenedUrlOptional = shortenedUrlRepository.findByShortUrl(shortUrl);
        if (shortenedUrlOptional.isEmpty()){
            throw new UrlNotFoundException("URL is not found");
        }
        ShortenedUrl shortenedUrl = shortenedUrlOptional.get();
        if(shortenedUrl.getExpiresAt() < System.currentTimeMillis()){
            throw new UrlNotFoundException("URL has expired");
        }
        UrlAccessLog urlAccessLog = new UrlAccessLog();
        urlAccessLog.setShortenedUrl(shortenedUrl);
        urlAccessLog.setAccessedAt(System.currentTimeMillis());
        urlAccessLogRepository.save(urlAccessLog);

        return shortenedUrl.getOriginalUrl();
    }
}
