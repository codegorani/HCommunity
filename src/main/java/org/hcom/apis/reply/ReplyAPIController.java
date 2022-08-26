package org.hcom.apis.reply;

import lombok.RequiredArgsConstructor;
import org.hcom.config.security.authorize.LoginUser;
import org.hcom.models.reply.dtos.request.ReplySaveRequestDTO;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.services.reply.ReplyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
public class ReplyAPIController {

    private final ReplyService replyService;

    @PostMapping("/api/v1/reply")
    public void replySaveControl(@RequestBody ReplySaveRequestDTO requestDTO, @LoginUser SessionUser sessionUser) {
        if(sessionUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NOT LOGIN");
        }
        replyService.replySaveService(sessionUser, requestDTO);
    }

    @DeleteMapping("/api/v1/reply/{replyIdx}")
    public void replyDelete(@PathVariable("replyIdx") Long idx, @LoginUser SessionUser sessionUser) {
        if (sessionUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NOT LOGIN");
        }
        replyService.replyDeleteService(idx, sessionUser);
    }
}
