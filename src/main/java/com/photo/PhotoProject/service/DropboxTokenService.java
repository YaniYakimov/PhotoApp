package com.photo.PhotoProject.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class DropboxTokenService {
    private String accessToken;

    public void saveAccessToken(String token) {
        this.accessToken = token;
    }

}
