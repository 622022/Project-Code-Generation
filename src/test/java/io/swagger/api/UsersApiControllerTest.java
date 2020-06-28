package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.Role;
import io.swagger.model.User;
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


class UsersApiControllerTest {
    @Autowired
    private MockMvc mvc;





    private User user= new User("username_10", "password_10", "email_10", Role.EMPLOYEE);
    private ObjectMapper mapper = new ObjectMapper();
    private String token;
    private MockedAccountType accountType;
    private MockedUser loginMockedUser;


    @BeforeEach
    public void loginToGetToken()throws Exception{
        //here we are performing login to get a token to pass it for authorization
        loginMockedUser= new MockedUser("username_1","password_1");
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
    public void creatNewUserReturn201Created() throws Exception{

        //ObjectMapper mapper = new ObjectMapper();
        this.mvc
                .perform(MockMvcRequestBuilders.post("/users")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.mapper.writeValueAsString(user)))

                .andExpect(status().isCreated());



    }
    @Test
    public void deletingUserShouldReturnOK() throws Exception {
     this.mvc
            .perform(MockMvcRequestBuilders.delete("/users/{userid}","8")
                    .header("Authorization",token)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))

             .andExpect(status().isOk());

    }

    @Test
    public void editingUserInformationShouldReturn200OK() throws Exception{
        this.mvc
                .perform(MockMvcRequestBuilders.put("/users/{userid}","7")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.mapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    public void getUserAccountByIdShouldReturn200Response()throws Exception{
        this.mvc
                .perform(MockMvcRequestBuilders.get("/users/{userid}/accounts","7")
                        .header("Authorization",token)
                )

                .andExpect(status().isOk());
    }

    @Test
    public void getAllUsersReturn200Response() throws Exception{
        this.mvc
                .perform(MockMvcRequestBuilders.get("/users")
                .header("Authorization",token)
                        )
                .andExpect(status().isOk());
    }

    @Test
    public void creatingAccountForSpecificUserReturns200Response()throws Exception{
        accountType= new MockedAccountType("Saving");
        this.mvc
                .perform(MockMvcRequestBuilders.post("/users/{userid}/accounts","7")
                .header ("Authorization",token)
                .contentType((MediaType.APPLICATION_JSON))
                .content(this.mapper.writeValueAsString(accountType)))
                .andExpect(status().isOk());
    }




}
 class MockedAccountType{
    public String accountType;

     public MockedAccountType(String accountType) {
         this.accountType = accountType;
     }
 }
