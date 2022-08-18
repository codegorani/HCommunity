package org.hcom.controllers.article;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.hcom.models.article.Article;
import org.hcom.models.article.dtos.response.ArticleDetailResponseDTO;
import org.hcom.models.article.dtos.response.ArticleListResponseDTO;
import org.hcom.services.article.ArticleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/article/new")
    public String articleNew() {
        return "article/article_new";
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
    public String viewArticle(@PathVariable("articleIdx") Long idx, Model model) {
        ArticleDetailResponseDTO responseDTO = articleService.articleViewService(idx);
        model.addAttribute("response", responseDTO);
        return "article/article-view";
    }

    @GetMapping("/article/{pageNum}")
    public String viewArticleList(@PathVariable("pageNum") int page, Model model) {
        Page<ArticleListResponseDTO> articlePage = articleService.getArticleListAsPage(page - 1);
        model.addAttribute("articleList", articlePage);
        return "article/article-main";
    }

    @GetMapping("/article")
    public String viewArticleMain(Model model) {
        Page<ArticleListResponseDTO> articlePage = articleService.getArticleListAsPage(0);
        model.addAttribute("articleList", articlePage);
        return "article/article-main";
    }
}
