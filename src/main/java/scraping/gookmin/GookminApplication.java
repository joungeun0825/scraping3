package scraping.gookmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class GookminApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(GookminApplication.class, args);
    }
}
