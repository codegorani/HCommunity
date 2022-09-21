package org.hcom.models.like.dtos.response;

import lombok.*;
import org.hcom.models.like.Like;

import java.time.format.DateTimeFormatter;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeListResponseByUserDTO {

    private String galleryKorName;
    private Long likeIdx;
    private Long articleIdx;
    private String likeDate;
    private String articleDate;
    private String title;
    private String writerNickname;
    private Long allLike;
    private Long allReply;

    public LikeListResponseByUserDTO(Like like) {
        this.galleryKorName = like.getArticle().getGallery().getGalleryKorName();
        this.likeIdx = like.getIdx();
        this.likeDate = like.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.articleIdx = like.getArticle().getIdx();
        this.articleDate = like.getArticle().getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.writerNickname = like.getArticle().getUser().getNickname();
        this.title = like.getArticle().getTitle();
    }
}
