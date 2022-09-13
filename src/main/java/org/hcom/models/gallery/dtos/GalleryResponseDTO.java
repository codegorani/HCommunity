package org.hcom.models.gallery.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hcom.models.gallery.Gallery;

@Getter
@Setter
@AllArgsConstructor
public class GalleryResponseDTO {

    private String galleryName;
    private String galleryKorName;
    private Long galleryTotalArticle;

    public GalleryResponseDTO(Gallery gallery, Long galleryTotalArticle) {
        this.galleryName = gallery.getGalleryName();
        this.galleryKorName = gallery.getGalleryKorName();
        this.galleryTotalArticle = galleryTotalArticle;
    }
}
