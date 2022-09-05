package org.hcom.models.article.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ArticleType {

    NOTICE("공지"),
    NORMAL("일반"),
    ;

    private final String type;
}
