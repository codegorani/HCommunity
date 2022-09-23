package org.hcom.controllers.article;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.hcom.config.security.authorize.LoginUser;
import org.hcom.models.article.dtos.response.ArticleDetailResponseDTO;
import org.hcom.models.article.dtos.response.ArticleListResponseDTO;
import org.hcom.models.gallery.dtos.GalleryResponseDTO;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.services.article.ArticleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RequiredArgsConstructor
@PropertySource("classpath:upload.properties")
@Controller
public class ArticleController {

    private final ArticleService articleService;

    @Value("${image.upload.path}")
    private String uploadPath;

    @GetMapping("/article/new/{galleryName}gall")
    public String articleNew(@LoginUser SessionUser sessionUser, @PathVariable("galleryName") String galleryName, Model model) {

        GalleryResponseDTO responseDTO = articleService.galleryResponseService(galleryName);
        model.addAttribute("gallery", responseDTO);
        return "article/article-new";
    }

    @PostMapping("/article/new/imageUpload")
    public void postImage(MultipartFile upload, HttpServletResponse res, HttpServletRequest req) throws Exception {

        OutputStream out = null;
        PrintWriter printWriter = null;

        res.setCharacterEncoding("utf-8");
        res.setContentType("text/html;charset=utf-8");

        try{
            // 랜덤 id 생성
            UUID uuid = UUID.randomUUID();
            String extension = FilenameUtils.getExtension(upload.getOriginalFilename());

            byte[] bytes = upload.getBytes();

            // 실제 이미지 저장 경로
            String imgUploadPath = uploadPath + File.separator + uuid + "." + extension;

            // 이미지 저장
            System.out.println(imgUploadPath);
            out = new FileOutputStream(imgUploadPath);
            out.write(bytes);
            out.flush();

            // ckEditor 로 전송
            printWriter = res.getWriter();
            String callback = req.getParameter("CKEditorFuncNum");
            String fileUrl = "/article/new/image/" + uuid + "." + extension;

            printWriter.println("<script type='text/javascript'>"
                    + "window.parent.CKEDITOR.tools.callFunction("
                    + callback+",'"+ fileUrl+"','이미지를 업로드하였습니다.')"
                    +"</script>");

            printWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(out != null) {
                    out.close();
                }
                if(printWriter != null) {
                    printWriter.close();
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/article/view/{articleIdx}")
    public String viewArticle(@PathVariable("articleIdx") Long idx, @LoginUser SessionUser sessionUser, Model model,
                              HttpServletRequest request, HttpServletResponse response) {
        ArticleDetailResponseDTO responseDTO = articleService.articleViewService(idx, sessionUser);
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("["+ idx.toString() +"]")) {
                articleService.updateView(idx);
                oldCookie.setValue(oldCookie.getValue() + "_[" + idx + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24); 							// 쿠키 시간
                response.addCookie(oldCookie);
            }
        } else {
            articleService.updateView(idx);
            Cookie newCookie = new Cookie("postView", "[" + idx + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24); 								// 쿠키 시간
            response.addCookie(newCookie);
        }
        model.addAttribute("article", responseDTO);
        model.addAttribute("sessionUser", sessionUser);
        return "article/article-view";
    }

    @GetMapping("/article/{gallery}gall")
    public String viewGalleryMain(Model model, @LoginUser SessionUser sessionUser, @PathVariable("gallery") String galleryName,
                                  @RequestParam(required = false) String search, @RequestParam(required = false) Long page) {
        int requestPage;
        if(page == null) {
            requestPage = 0;
        } else {
            requestPage = (int) (page - 1);
        }
        Page<ArticleListResponseDTO> articlePage = articleService.getArticleListAsPage(requestPage, galleryName, sessionUser, search);
        GalleryResponseDTO responseDTO = articleService.galleryResponseService(galleryName);
        model.addAttribute("gallery", responseDTO);
        model.addAttribute("articleList", articlePage);
        model.addAttribute("sessionUser", sessionUser);
        return "article/gallery/gallery-main";
    }

    @GetMapping("/article")
    public String viewArticleMain(Model model, @LoginUser SessionUser sessionUser) {
        model.addAttribute("sessionUser", sessionUser);
        return "article/article-main";
    }
}
