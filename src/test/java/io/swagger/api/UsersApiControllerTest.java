package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.content.Role;
import io.swagger.model.content.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UsersApiControllerTest {
    @Autowired
    private MockMvc mvc;
    private ObjectMapper mapper = new ObjectMapper();
    private Token utility;
    private User user = new User("username_10", "password_10", "email_10", Role.EMPLOYEE);
    private String employeeToken;
    private String customerToken;


    @BeforeEach
    public void loginToGetToken() throws Exception {
        utility = new Token(mvc, mapper);
        employeeToken = utility.getTokenFromSpecificUser("username_1", "password_1");
        customerToken = utility.getTokenFromSpecificUser("username_9", "password_9");
    }


    @Test
    public void creatNewUserReturn201Created() throws Exception {

        this.mvc
                .perform(post("/users")
                        .header("Authorization", employeeToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.mapper.writeValueAsString(user)))

                .andExpect(status().isCreated());


    }

    @Test
    public void deletingUserShouldReturnOK() throws Exception {
        this.mvc
                .perform(delete("/users/{userid}", "5")
                        .header("Authorization", employeeToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))

                .andExpect(status().isAccepted());

    }

    @Test
    public void editingUserInformationShouldReturn200OK() throws Exception {
        this.mvc
                .perform(put("/users/{userid}", "7")
                        .header("Authorization", employeeToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.mapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    public void getUserAccountByIdPerformedByEmployeeShouldReturn200Response() throws Exception {
        this.mvc
                .perform(get("/users/{userid}/accounts", "7")
                        .header("Authorization", employeeToken)
                )

                .andExpect(status().isOk());
    }

    @Test
    public void getUserAccountByIdPerformedByCustomerShouldReturn200Response() throws Exception {
        this.mvc
                .perform(get("/users/{userid}/accounts", "9")
                        .header("Authorization", customerToken)
                )

                .andExpect(status().isOk());
    }

    @Test
    public void getUserAccountByIdPerformedByCustomerForDifferentCustomerShouldReturn401Response() throws Exception {
        this.mvc
                .perform(get("/users/{userid}/accounts", "6")
                        .header("Authorization", customerToken)
                )

                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getAllUsersReturn200Response() throws Exception {
        this.mvc
                .perform(get("/users")
                        .header("Authorization", employeeToken)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void creatingAccountForSpecificUserReturns201Response() throws Exception {
        MockedAccountType accountType = new MockedAccountType("Saving");
        this.mvc
                .perform(post("/users/{userid}/accounts/", 7)
                        .header("Authorization", employeeToken)
                        .param("accountType", "SAVING")
                        .contentType((MediaType.APPLICATION_JSON)))
                .andExpect(status().isCreated());
    }

}

class MockedAccountType {
    public String accountType;

    public MockedAccountType(String accountType) {
        this.accountType = accountType;
    }
}
