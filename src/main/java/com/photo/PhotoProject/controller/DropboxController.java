package com.photo.PhotoProject.controller;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.sharing.ListSharedLinksResult;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import com.photo.PhotoProject.service.DropboxTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DropboxController {

    @Autowired
    private DropboxTokenService dropboxTokenService;
    @GetMapping("/dropbox/photos")
    public List<String> listFiles() {
        String ACCESS_TOKEN = dropboxTokenService.getAccessToken();
        DbxRequestConfig config = DbxRequestConfig.newBuilder("Photography-app-02").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

        List<String> photoUrls = new ArrayList<>();
        try {
            ListFolderResult result = client.files().listFolder("/HomePage_Photography_App");
            for (Metadata metadata : result.getEntries()) {
                ListSharedLinksResult sharedLinksResult = client.sharing().listSharedLinksBuilder()
                        .withPath(metadata.getPathLower())
                        .withDirectOnly(true)
                        .start();

                if (!sharedLinksResult.getLinks().isEmpty()) {
                    SharedLinkMetadata existingLink = sharedLinksResult.getLinks().get(0);
                    photoUrls.add(existingLink.getUrl());
                } else {
                    SharedLinkMetadata sharedLinkMetadata = client.sharing().createSharedLinkWithSettings(metadata.getPathLower());
                    photoUrls.add(sharedLinkMetadata.getUrl());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return photoUrls;
    }
}
