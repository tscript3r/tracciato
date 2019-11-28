package pl.tscript3r.tracciato.user;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.tscript3r.tracciato.ReplaceCamelCaseAndUnderscores;
import pl.tscript3r.tracciato.infrastructure.response.ResponseResolver;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.USER_MAPPING;

@DisplayName("User controller")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
@WebMvcTest(UserSpringController.class)
@WithMockUser("spring")
@ContextConfiguration(classes = {ResponseResolver.class})
class UserSpringControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Disabled
    @Test
    void registerUser_Should_ReturnHttpStatus201_When_UserRegistrationSucceed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(USER_MAPPING)
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

}