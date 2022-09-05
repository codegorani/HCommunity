package org.hcom.models.article.dtos.response;

import lombok.*;
import org.hcom.models.article.Article;
import org.hcom.models.reply.Reply;
import org.hcom.models.reply.dtos.response.ReplyResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailResponseDTO {
    private Long idx;
    private String nickname;
    private String title;
    private String contents;
    private LocalDateTime createdDate;
    private List<ReplyResponseDTO> replyList;
    private Long allLike;
    private Long isLike;
    private int allReply;
    private String galleryName;

    public ArticleDetailResponseDTO(Article article, List<Reply> replyList) {
        this.idx = article.getIdx();
        this.nickname = article.getUser().getNickname();
        this.title = article.getTitle();
        this.contents = article.getContents();
        this.createdDate = article.getCreatedDate();
        this.replyList = replyList.stream().map(ReplyResponseDTO::new).collect(Collectors.toList());
        this.allReply = replyList.size();
        this.galleryName = article.getGallery().getGalleryName();
    }
}
