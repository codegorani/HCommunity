package org.hcom.models.gallery.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hcom.models.gallery.Gallery;

@Getter
@Setter
@NoArgsConstructor
public class GallerySaveRequestDTO {

    private String galleryName;
    private String galleryKorName;

    public Gallery toEntity() {
        return Gallery
                .builder()
                .galleryName(galleryName)
                .galleryKorName(galleryKorName)
                .build();
    }
}
