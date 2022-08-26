package org.hcom.models.reply.dtos.response;

import lombok.*;
import org.hcom.models.reply.Reply;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyResponseDTO {
    private Long idx;
    private String nickname;
    private String replyContent;
    private String createdDate;

    public ReplyResponseDTO(Reply reply) {
        this.idx = reply.getIdx();
        this.nickname = reply.getUser().getNickname();
        this.replyContent = reply.getReplyContent();
        this.createdDate = reply.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
