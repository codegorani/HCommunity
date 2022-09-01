package org.hcom.models.reply.dtos.response;

import lombok.Getter;
import lombok.Setter;
import org.hcom.models.reply.Reply;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReplyListResponseByUserDTO {

    private Long idx;
    private Long articleIdx;
    private String replyContent;
    private LocalDateTime createdDate;

    public ReplyListResponseByUserDTO(Reply reply) {
        this.idx = reply.getIdx();
        this.articleIdx = reply.getArticle().getIdx();
        this.replyContent = reply.getReplyContent();
        this.createdDate =reply.getCreatedDate();
    }
}
