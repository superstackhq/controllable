package one.superstack.controllable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Controllable {

    public static void main(String[] args) {
        SpringApplication.run(Controllable.class, args);
    }
}
