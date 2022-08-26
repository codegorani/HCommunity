package org.hcom.models.reply.dtos.request;

import lombok.*;
import org.hcom.models.article.Article;
import org.hcom.models.reply.Reply;
import org.hcom.models.user.User;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplySaveRequestDTO {
    private Long idx;
    private String replyContent;

    public Reply toEntity(Article article, User user) {
        return Reply.builder()
                .article(article)
                .user(user)
                .replyContent(replyContent)
                .build();
    }
}
