package com.photo.PhotoProject.controller;

import com.dropbox.core.*;
import com.photo.PhotoProject.service.DropboxTokenService;
import com.photo.PhotoProject.util.NoopSessionStore;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class DropboxAuthController {
    private final String APP_KEY;
    private final String APP_SECRET;
    private final String REDIRECT_URI = "http://localhost:8080/dropbox/callback";
    @Autowired
    private DropboxTokenService dropboxTokenService;

    DropboxAuthController() {
        this.APP_KEY = System.getenv("APP_KEY");
        this.APP_SECRET = System.getenv("APP_SECRET");
    }

    @GetMapping("/dropbox/auth")
    public void startDropboxAuth(HttpServletResponse response) throws IOException {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("Photography-app-02").build();
        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

        DbxWebAuth webAuth = new DbxWebAuth(config, appInfo);
        NoopSessionStore noopSessionStore = new NoopSessionStore();
        DbxWebAuth.Request webAuthRequest = DbxWebAuth.newRequestBuilder()
                .withRedirectUri(REDIRECT_URI, noopSessionStore)
                .build();

        String authorizeUrl = webAuth.authorize(webAuthRequest);
        response.sendRedirect(authorizeUrl);
    }
    @GetMapping("/dropbox/callback")
    public void handleDropboxCallback(@RequestParam String code, HttpServletResponse response) throws IOException {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("Photography-app-02").build();
        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        DbxWebAuth webAuth = new DbxWebAuth(config, appInfo);

        try {

            DbxAuthFinish authFinish = webAuth.finishFromCode(code, REDIRECT_URI);
            String accessToken = authFinish.getAccessToken();

            this.dropboxTokenService.setAccessToken(accessToken);
            response.sendRedirect("/home");
        } catch (Exception ex) {
            response.sendRedirect("/error");
        }
    }
}
