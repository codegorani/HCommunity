package org.hcom;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hcom.models.user.User;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.models.user.enums.UserGrade;
import org.hcom.models.user.enums.UserStatus;
import org.hcom.models.user.support.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class UserCRUDTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void doPre() {
        userRepository.deleteAll();
        assertThat(userRepository.findAll().size()).isEqualTo(0);
    }

    @After
    public void doPost() {
        userRepository.deleteAll();
        assertThat(userRepository.findAll().size()).isEqualTo(0);
    }

    @DisplayName("[User/Create] : User create test")
    @Test
    public void userCreateTest() throws Exception {
        Map<String, String> contents = new HashMap<>();
        contents.put("username", "hjhearts");
        contents.put("password", "asd5689");
        contents.put("nickname", "하위");
        contents.put("lastName", "한");
        contents.put("firstName", "주성");
        contents.put("birth", "1995-10-29");
        contents.put("email", "none@none.com");
        contents.put("phoneNum", "010-1234-5678");
        contents.put("telNum", "");
        contents.put("address1", "경기도 오산시 123-45");
        contents.put("address2", "");

        mockMvc.perform(post("/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(contents)))
                .andExpect(status().isOk());

        User user = userRepository.findAll().get(0);
        assertThat(user.getNickname()).isEqualTo("하위");
        assertThat(user.getUserGrade()).isEqualTo(UserGrade.BRONZE);
        assertThat(user.getUserStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @DisplayName("[User/Read] : User read test")
    @WithMockUser(username = "hjhearts", password = "asd5689")
    @Test
    public void userReadTest() throws Exception {
        Map<String, String> contents = new HashMap<>();
        contents.put("username", "hjhearts");
        contents.put("password", "asd5689");
        contents.put("nickname", "하위");
        contents.put("lastName", "한");
        contents.put("firstName", "주성");
        contents.put("birth", "1995-10-29");
        contents.put("email", "none@none.com");
        contents.put("phoneNum", "010-1234-5678");
        contents.put("telNum", "");
        contents.put("address1", "경기도 오산시 123-45");
        contents.put("address2", "");

        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contents)))
                .andExpect(status().isOk());

        mockMvc.perform(formLogin().user("hjhearts").password("asd5689"))
                .andExpect(status().is3xxRedirection());
        User user = userRepository.findAll().get(0);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new SessionUser(user));
        String expectByNickname = "$.data.nickname == '%s'";

        MvcResult result = mockMvc.perform(get("/api/v1/user/hjhearts").session(session)).andExpect(status().isOk()).andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }
}
