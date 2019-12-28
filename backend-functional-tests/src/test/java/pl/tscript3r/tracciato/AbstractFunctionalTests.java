package pl.tscript3r.tracciato;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.tscript3r.tracciato.route.RouteFeatures;
import pl.tscript3r.tracciato.user.UserFeatures;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("development")
public abstract class AbstractFunctionalTests {

    @Rule
    public WireMockRule contactsServiceMock = new WireMockRule(options().port(8777));

    @Autowired
    protected UserFeatures userFeatures;

    @Autowired
    protected RouteFeatures routeFeatures;

}