package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.AccountObject;
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
    private AccountObject account;
    private String specifcAccountIban;


    @BeforeEach
    public void loginForToken()throws Exception{
        //here we are performing login to get a token to pass it for authorization
        loginMockedUser=new MockedUser("username_1","password_1");
        MvcResult loginResult=
                this.mvc
                        .perform(MockMvcRequestBuilders.post("/login")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(this.mapper.writeValueAsString(loginMockedUser)))
                        .andReturn();
        String responseContent=loginResult.getResponse().getContentAsString();
        String[] dividedResponse = responseContent.split("\"tokenValue\":\"");
        String [] clearingToken= dividedResponse[1].split("\"");
        token = "Bearer "+clearingToken[0];

        //get a valid existing account is required for some tests
        MvcResult oneAccountResult=
                this.mvc
                        .perform(MockMvcRequestBuilders.get("/accounts")
                                .header("Authorization",token)
                                .param("limit","1"))
                        .andReturn();
        String ibanResponseContent= oneAccountResult.getResponse().getContentAsString();
        try {
            String[] dividedAccountResponse = ibanResponseContent.split(",");
            specifcAccountIban = dividedAccountResponse[0].split(":\"")[1].substring(0, dividedAccountResponse[0].split(":\"")[1].length() - 1);
        }
        catch (Exception ex){
            System.out.println(String.format("Something went wrong reading the account object: %s",ex.getMessage() ));
        }



    }

    @Test
    public void getAllAccountsShouldReturn200Response()throws Exception{
        this.mvc
                .perform(MockMvcRequestBuilders.get("/accounts")
                        .header("Authorization",token)
                )
                .andExpect(status().isOk());
        MvcResult result= this.mvc
                        .perform(MockMvcRequestBuilders.get("/accounts")
                                .header("Authorization",token)
                        )
                        .andReturn();

    }

    @Test
    public void gettingNonExistingAccountReturns204Response()throws Exception{
        this.mvc
                .perform(MockMvcRequestBuilders.get("/accounts/NL12ING01234567892")
                        .header("Authorization",token))
                .andExpect(status().isNoContent());

    }
    @Test
    public void gettingExistingAccountReturns200Response()throws Exception{
        this.mvc
                .perform(MockMvcRequestBuilders.get("/accounts/{Iban}",specifcAccountIban)
                        .header("Authorization",token))
                .andExpect(status().isOk());

    }

    @Test
    public void editAccountOfSpecificIbanReturns200Response()throws Exception{
        //account = new AccountObject(3000,"");
        //Integer amount, Integer ownerId, AccountObject.TypeEnum type, AccountObject.StatusEnum status, Double transactionLimit, Integer dayLimit, Integer absolutelimit
        this.mvc
                .perform(MockMvcRequestBuilders.put("/accounts/{IBAN}",specifcAccountIban)
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.mapper.writeValueAsString(account)))
                .andExpect(status().isOk());

    }

    @Test
    public void deleteAccountReturns200Response()throws Exception{

    }
}