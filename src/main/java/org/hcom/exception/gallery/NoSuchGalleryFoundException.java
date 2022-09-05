package org.hcom.exception.gallery;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoSuchGalleryFoundException extends RuntimeException{

    public NoSuchGalleryFoundException(String galleryName) {
        super("gallery with galleryName:[" + galleryName + "] is not found");
    }
}
