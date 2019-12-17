package pl.tscript3r.tracciato.user;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.tscript3r.tracciato.ReplaceCamelCaseAndUnderscores;
import pl.tscript3r.tracciato.user.api.UserDto;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.USER_MAPPING;

@Disabled
@DisplayName("User controller")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserSpringControllerTest {

    public static final String NEW_USER_USERNAME = "BrandNewUser";
    public static final String NEW_USER_PASSWORD = "password";
    public static final String NEW_USER_EMAIL = "brandnewuser@email.com";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void registerUser_Should_ReturnHttpStatus201_When_UserRegistrationSucceed() throws Exception {
        // given
        UserDto requestUser = new UserDto();
        requestUser.setUsername(NEW_USER_USERNAME);
        requestUser.setPassword(NEW_USER_PASSWORD);
        requestUser.setEmail(NEW_USER_EMAIL);

        // when
        ResponseEntity<String> response = restTemplate.postForEntity(getUsersUrl(),
                requestUser, String.class);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        var responseDto = (ResponseDto<UserDto>) response.getBody();
//        assertTrue(requestUser.getUsername()
//                .equalsIgnoreCase(responseDto.getPayload().getUsername()));
    }

    String getUsersUrl() throws MalformedURLException {
        return new URL("http://localhost:" + port + "/" + USER_MAPPING).toString();
    }

}