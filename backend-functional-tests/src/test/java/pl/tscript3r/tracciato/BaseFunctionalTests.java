package pl.tscript3r.tracciato;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.tscript3r.tracciato.steps.UserFeatures;

import java.io.IOException;
import java.nio.charset.Charset;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.springframework.util.ResourceUtils.getFile;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("development")
public abstract class BaseFunctionalTests {

    @Rule
    public WireMockRule contactsServiceMock = new WireMockRule(options().port(8777));

    @Autowired
    protected UserFeatures userFeatures;

    @TestConfiguration
    @ComponentScan("pl.tscript3r.tracciato.steps")
    public static class StepsConfiguration {
    }

    public static String readFile(String path) {
        try {
            return FileUtils.readFileToString(getFile("classpath:" + path), Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException("File cannot be read");
        }
    }

}