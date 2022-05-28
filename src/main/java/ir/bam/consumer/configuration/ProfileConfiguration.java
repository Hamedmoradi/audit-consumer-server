package ir.bam.consumer.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ProfileConfiguration {

    private static Logger LOGGER = LoggerFactory.getLogger(ProfileConfiguration.class);

    @Profile(value = "dev")
    @Bean
    public void devconfig() {
        LOGGER.info("Welcome to Development profile .");
    }

    @Profile(value = "uat")
    @Bean
    public void uatconfig() {
        LOGGER.info("Welcome to the UAT profile ..");
    }

    @Profile(value = "test")
    @Bean
    public void testconfig() {
        LOGGER.info("Welcome to testing profile ..");
    }

    @Profile(value = "prod")
    @Bean
    public void prodconfig() {
        LOGGER.info("Welcome to the Production profile ..");
    }
}