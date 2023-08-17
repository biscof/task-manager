package hexlet.code.config;

import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.Config;
import com.rollbar.spring.webmvc.RollbarSpringConfigBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


@Configuration
@ComponentScan({ "hexlet.code", "com.rollbar.spring" })
public class RollbarConfig {

    @Value("${rollbar-token}")
    private String rollbarToken;

    private final Environment environment;

    public RollbarConfig(Environment environment) {
        this.environment = environment;
    }
    @Bean
    public Rollbar rollbar() {
        Rollbar rollbar = new Rollbar(getRollbarConfigs(rollbarToken));
        rollbar.debug("Here is a test debug message");
        return rollbar;
    }

    private Config getRollbarConfigs(String accessToken) {
        String activeProfile = environment.getProperty("spring.profiles.active");
        if (activeProfile != null
                && (activeProfile.equals("dev") || activeProfile.equals("prod"))) {
            return RollbarSpringConfigBuilder.withAccessToken(accessToken)
                    .environment(activeProfile)
                    .build();
        } else {
            // If the active profile is other than "dev" or "prod" (say "test"),
            // then don't use Rollbar
            return RollbarSpringConfigBuilder.withAccessToken("903e6c0ede76926b7e6cf").build();
//            return RollbarSpringConfigBuilder.withAccessToken(accessToken)
//                    .environment(activeProfile)
//                    .build();
        }
    }
}
