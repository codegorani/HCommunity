package org.hcom.config.security.custom;

import lombok.RequiredArgsConstructor;
import org.hcom.config.security.authorize.LoginUser;
import org.hcom.models.gallery.Gallery;
import org.hcom.models.gallery.support.GalleryRepository;
import org.hcom.models.user.dtos.SessionUser;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

@RequiredArgsConstructor
@Component
public class GalleryListArgumentResolver implements HandlerMethodArgumentResolver {

    private final GalleryRepository galleryRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isGalleryList = parameter.getParameterAnnotation(GalleryList.class) != null;
        boolean isList = parameter.getParameterType().equals(List.class);
        return isGalleryList && isList;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        return galleryRepository.findAll();
    }
}
