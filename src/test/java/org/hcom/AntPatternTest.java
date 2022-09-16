package org.hcom;

import org.junit.Test;
import org.springframework.util.AntPathMatcher;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AntPatternTest {

    @Test
    public void antPatternTest() {
        // **
        assertThat(checkAntPattern("/board/**", "/board/football")).isEqualTo(true);
        assertThat(checkAntPattern("/board/**", "/board/baseball/a")).isEqualTo(true);
        assertThat(checkAntPattern("/board/**", "/board/baseball/a/b/c")).isEqualTo(true);
        assertThat(checkAntPattern("/board/**", "/board")).isEqualTo(true);
        assertThat(checkAntPattern("/board/**", "/board/something.jpg")).isEqualTo(true);
        assertThat(checkAntPattern("/board/**", "/baseball/board")).isEqualTo(false);

        assertThat(checkAntPattern("/**/board/**", "/a/b/c/board/d/e/f")).isEqualTo(true);
        assertThat(checkAntPattern("/**/board", "/a/b/c/board/baseball")).isEqualTo(false);

        // *
        assertThat(checkAntPattern("/board/*", "/board")).isEqualTo(false);
        assertThat(checkAntPattern("/board/*", "/board/baseball")).isEqualTo(true);
        assertThat(checkAntPattern("/board/*", "/board/baseball/a")).isEqualTo(false);
        assertThat(checkAntPattern("/board/*", "/board/baseball/a/b/c")).isEqualTo(false);

        assertThat(checkAntPattern("/board/*ball", "/board/tennis")).isEqualTo(false);
        assertThat(checkAntPattern("/board/*ball", "/board/baseball")).isEqualTo(true);

        assertThat(checkAntPattern("/board*/*ball", "/boardabcd/baseball")).isEqualTo(true);
        assertThat(checkAntPattern("/board*/*ball", "/board/baseball")).isEqualTo(true);

        // ?
        assertThat(checkAntPattern("/board/?", "/board")).isEqualTo(false);
        assertThat(checkAntPattern("/board/?", "/board/a")).isEqualTo(true);
        assertThat(checkAntPattern("/board/?", "/board/aa")).isEqualTo(false);
        assertThat(checkAntPattern("/board/??ball", "/board/abball")).isEqualTo(true);
        assertThat(checkAntPattern("/board/??ball", "/board/abcball")).isEqualTo(false);
        assertThat(checkAntPattern("/board-?/???.jpg", "/board-a/abc.jpg")).isEqualTo(true);
        assertThat(checkAntPattern("/board-?/???.jpg", "/board-a/abc.jpeg")).isEqualTo(false);
    }

    private boolean checkAntPattern(String pattern, String msg) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return antPathMatcher.match(pattern, msg);
    }
}
