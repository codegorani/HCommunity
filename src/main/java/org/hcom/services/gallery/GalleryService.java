package org.hcom.services.gallery;

import lombok.RequiredArgsConstructor;
import org.hcom.exception.role.NoRoleToServiceException;
import org.hcom.models.gallery.dtos.GallerySaveRequestDTO;
import org.hcom.models.gallery.support.GalleryRepository;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.models.user.enums.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GalleryService {

    private final GalleryRepository galleryRepository;

    @Transactional
    public void gallerySaveService(GallerySaveRequestDTO requestDTO, SessionUser sessionUser) {
        if (sessionUser.getUserRole().equals(UserRole.USER)) {
            throw new NoRoleToServiceException(UserRole.ADMIN, UserRole.USER);
        }
        galleryRepository.save(requestDTO.toEntity());
    }
}
