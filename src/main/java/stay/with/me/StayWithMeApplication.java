package stay.with.me;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling   
@SpringBootApplication
public class StayWithMeApplication {

    public static void main(String[] args) {
        SpringApplication.run(StayWithMeApplication.class, args);
    }

}
