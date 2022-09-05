package org.hcom.models.gallery.support;

import org.hcom.models.gallery.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {

    Optional<Gallery> findByGalleryName(String galleryName);
}
