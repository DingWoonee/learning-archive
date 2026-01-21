package sample.testcafekiosk.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TestCafekioskApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestCafekioskApplication.class, args);
    }

}
