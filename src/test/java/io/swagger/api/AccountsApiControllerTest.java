package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountsApiControllerTest {
    @Autowired
    private MockMvc mvc;

    private ObjectMapper mapper= new ObjectMapper();
    private String token;
    private MockedUser loginMockedUser;

    @BeforeEach
    public void loginToGetToken()throws Exception{
        //here we are performing login to get a token to pass it for authorization
        loginMockedUser= new MockedUser("username_2","password_2");
        MvcResult result =
                this.mvc
                        .perform(MockMvcRequestBuilders.post("/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.mapper.writeValueAsString(loginMockedUser)))
                        .andReturn();

        String content = result.getResponse().getContentAsString();
        String[] responseParts=content.split("\"tokenValue\":\"");
        String t = responseParts[1];
        String[] clearTheToken=responseParts[1].split("\"");
        token= "Bearer "+clearTheToken[0];
    }

    @Test
    public void getAllAccountsShouldReturn200Response()throws Exception{
        this.mvc
                .perform(MockMvcRequestBuilders.get("/accounts")
                        .header("Authorization",token)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void getSpecificAccountShouldReturn200Response()throws Exception{
        this.mvc
                .perform(MockMvcRequestBuilders.get("/accounts/{IBAN}","NL16ABNA6403737190")
                        .header("Authorization",token)
                )
                .andExpect(status().isOk());

    }

    @Test
    public void editAccountOfSpecificIbanReturns200Response()throws Exception{
        this.mvc
                .perform(MockMvcRequestBuilders.put("/accounts/{IBAN}","NL16ABNA6403737190")
                        .header("Authorization",token))
                .andExpect(status().isOk());

    }

    @Test
    public void deleteAccountReturns200Response()throws Exception{

    }
}