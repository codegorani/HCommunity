package org.hcom.models.article;

import lombok.*;
import org.hcom.models.common.BaseTimeEntity;
import org.hcom.models.user.User;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Article extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long idx;

    private String title;

    private String contents;

    @JoinColumn
    @ManyToOne
    private User user;
}
