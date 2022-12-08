package MidTermProject;

import MidTermProject.controller.dto.AccountBalanceDTO;
import MidTermProject.controller.impl.BasicAccountController;
import MidTermProject.model.Money;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BasicAccountControllerTests {

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

    //WithMockUser para simular autenticación de un usuario con determinados roles, no tiene porqué existir en la
    //base de datos
    @Test
    @WithMockUser(username="user01",roles={"ADMIN"})
    void getAccountBalanceById_validId_balance() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/accounts/balance/2"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //assertTrue(mvcResult.getResponse().getContentAsString().contains("amount"));
    }

    //Probamos con un usuario existente para comprobar que solo puede acceder a cuentas propias, y que falla
    //cuando la cuenta no le pertenece
    @Test
    @WithMockUser(username="test_ac03",roles={"ACCOUNT_HOLDER"})
    void getOwnAccountBalance_invalidAccount_notFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/own_accounts/balance/2"))
                .andExpect(status().isNotFound())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //assertTrue(mvcResult.getResponse().getContentAsString().contains("amount"));
    }

    @Test
    @WithMockUser(username="test_ac01",roles={"ACCOUNT_HOLDER"})
    void getOwnAccountBalance_validAccount_balance() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/own_accounts/balance/2"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("amount"));
    }

    //Validar que solo el rol de administrador puede actualizar balances
    @Test
    @WithMockUser(username="user01",roles={"ADMIN"})
    void updateBalance_validUser_balanceUpdated() throws Exception {
        AccountBalanceDTO accountBalanceDTO = new AccountBalanceDTO(new Money(BigDecimal.valueOf(2222)));
        String body = objectMapper.writeValueAsString(accountBalanceDTO);

        mockMvc.perform(patch("/api/accounts/balance_modify/2").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        MvcResult mvcResult = mockMvc.perform(get("/api/accounts/balance/2"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("2222"));

    }

    @Test
    @WithMockUser(username="test_ac03",roles={"ACCOUNT_HOLDER"})
    void updateBalance_invalidUser_isUnauthorized() throws Exception {
        AccountBalanceDTO accountBalanceDTO = new AccountBalanceDTO(new Money(BigDecimal.valueOf(2222)));
        String body = objectMapper.writeValueAsString(accountBalanceDTO);

        mockMvc.perform(patch("/api/accounts/balance_modify/2").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
//
//        MvcResult mvcResult = mockMvc.perform(get("/api/accounts/balance/2"))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andReturn();
//
//        assertTrue(mvcResult.getResponse().getContentAsString().contains("2222"));

    }

    @Test
    @WithMockUser(username="test_tp01",roles={"THIRD_PARTY"})
    void thirdPartySendReceive_validInput_balanceUpdated() throws Exception {
//        double amount = 35.0;
//        Integer accountId = 2;
//        String secretKey = "secretkey";

        BigDecimal initialBalance = basicAccountRepository.findById(2).get().getBalance().getAmount();
        BigDecimal finalBalance = basicAccountRepository.findById(2).get().getBalance().getAmount()
                .add(BigDecimal.valueOf(35));

        System.out.println(finalBalance);
        mockMvc.perform(patch("/api/accounts/third_party/35/2/secretkey"))
                .andExpect(status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        MvcResult mvcResult = mockMvc.perform(get("/api/accounts/balance/2"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains(String.valueOf(finalBalance)));
    }




}
