package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoginApiControllerTest {
    @Autowired
    private MockMvc mvc;

    private MockedUser loginMockedUser;
    private ObjectMapper mapper;

    @Test
    public void loginRequestShouldReturn200Response() throws Exception{
        mapper=new ObjectMapper();
        loginMockedUser= new MockedUser("username_1","password_1");
        this.mvc
                .perform(MockMvcRequestBuilders.post("/login")

                        .contentType((MediaType.APPLICATION_JSON))
                        .content(this.mapper.writeValueAsString(loginMockedUser)))
                .andExpect(status().isOk());
    }

}
class MockedUser{
    public String username;
    public String password;

    public MockedUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
}