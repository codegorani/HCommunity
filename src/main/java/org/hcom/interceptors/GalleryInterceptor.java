package org.hcom.interceptors;

import lombok.RequiredArgsConstructor;
import org.hcom.config.security.custom.GalleryList;
import org.hcom.models.gallery.Gallery;
import org.hcom.models.gallery.support.GalleryRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequiredArgsConstructor
@Component
public class GalleryInterceptor implements HandlerInterceptor {

    private final GalleryRepository galleryRepository;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView != null) {
            List<Gallery> galleryList = galleryRepository.findAll();
            modelAndView.getModelMap()
                    .addAttribute("galleryList", galleryList);
        }
    }
}
