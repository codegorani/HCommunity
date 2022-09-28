package org.hcom.models.reply.dtos.response;

import lombok.Getter;
import lombok.Setter;
import org.hcom.models.reply.Reply;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ReplyListResponseByUserDTO {

    private Long idx;
    private String galleryKorName;
    private Long articleIdx;
    private String articleTitle;
    private String replyContent;
    private String createdDate;

    public ReplyListResponseByUserDTO(Reply reply) {
        this.idx = reply.getIdx();
        this.galleryKorName = reply.getArticle().getGallery().getGalleryKorName();
        this.articleIdx = reply.getArticle().getIdx();
        this.articleTitle = reply.getArticle().getTitle();
        this.replyContent = reply.getReplyContent();
        this.createdDate =reply.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
