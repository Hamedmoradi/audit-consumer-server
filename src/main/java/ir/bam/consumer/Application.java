package ir.bam.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@Configuration
@EnableKafka

public class Application {

public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
}

}

