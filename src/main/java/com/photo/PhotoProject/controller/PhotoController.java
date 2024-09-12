package com.photo.PhotoProject.controller;

import com.photo.PhotoProject.service.DropboxTokenService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class PhotoController {

    @Autowired
    private DropboxTokenService dropboxTokenService;

    private final String DROPBOX_FOLDER_PATH = "/Photo-app/HomePage";

    @GetMapping("/home")
    public void home(HttpServletResponse response) throws IOException {
        String accessToken = dropboxTokenService.getAccessToken();

        if (accessToken == null) {
            response.sendRedirect("/dropbox/auth");
        } else {
            response.sendRedirect("/dropbox/photos");
        }
    }

}
