package com.photo.PhotoProject.util;

import com.dropbox.core.DbxSessionStore;

public class NoopSessionStore implements DbxSessionStore {

    public NoopSessionStore() {
        super();
    }
    @Override
    public String get() {
        return null;
    }

    @Override
    public void set(String s) {

    }

    @Override
    public void clear() {

    }
}
