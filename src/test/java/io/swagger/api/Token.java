package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureMockMvc
public class Token {
    private MockMvc mvc;
    private ObjectMapper mapper;

    public Token(MockMvc mvc, ObjectMapper mapper) {
        this.mapper = mapper;
        this.mvc = mvc;
    }

    public String getTokenFromSpecificUser(String username , String password)throws Exception{
        MockedUser loginMockedUser = new MockedUser(username, password);
        String token;
        MvcResult loginResult =
                this.mvc
                        .perform(post("/login")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(this.mapper.writeValueAsString(loginMockedUser)))
                        .andReturn();
        String responseContent = loginResult.getResponse().getContentAsString();
        String[] dividedResponse = responseContent.split("\"tokenValue\":\"");
        String[] clearingToken = dividedResponse[1].split("\"");
        token = "Bearer " + clearingToken[0];

        return token;
    }
}
