package MidTermProject;

import MidTermProject.controller.dto.AccountBalanceDTO;
import MidTermProject.controller.impl.BasicAccountController;
import MidTermProject.model.Accounts.Savings;
import MidTermProject.model.Address;
import MidTermProject.model.Money;
import MidTermProject.model.Users.AccountHolder;
import MidTermProject.model.Users.Roles;
import MidTermProject.model.Users.Status;
import MidTermProject.repository.BasicAccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
public class SavingsControllerTests {
    @Autowired
    BasicAccountController basicAccountController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    BasicAccountRepository basicAccountRepository;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void saveSavings_validAccount_accountSaved() throws Exception {
        Address ad = new Address("Urgell",25,"45343483",4845);
        Address adOpt= new Address("Urgell",25,"4561345",4845);
        Date today = new Date(LocalDate.now().getYear(),LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
        AccountHolder ah = new AccountHolder(3, "test_ac02","passA02", String.valueOf(Roles.ACCOUNT_HOLDER),
                new Date(1985,4,5),ad, Optional.of(adOpt));

        Savings sa = new Savings(new Money(BigDecimal.valueOf(2244)),
                ah,
                Optional.empty(),
                today,
                "SECRET--KEY",
                new Money(BigDecimal.valueOf(50)),
                BigDecimal.valueOf(0.002),
                Status.ACTIVE);

        String body = objectMapper.writeValueAsString(sa);
        System.out.println(body);

        mockMvc.perform(post("/api/accounts/savings").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        MvcResult mvcResult = mockMvc.perform(get("/api/accounts/balance/3"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("2244"));
//
//        mockMvc.perform(delete("/api/courses/testing"))
//                .andExpect(status().isNoContent())
//                .andReturn();
    }

//    @Test
//    void saveCourse_invalidCourse_unprocessableEntityResponse() throws Exception {
//        Course course = new Course("Math", 100, "A1", "2 weeks", null);
//        String body = objectMapper.writeValueAsString(course);
//
//        mockMvc.perform(post("/api/courses").content(body).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isUnprocessableEntity())
//                .andReturn();
//    }
}