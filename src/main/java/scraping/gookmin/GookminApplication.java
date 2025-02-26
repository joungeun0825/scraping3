package scraping.gookmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class GookminApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(GookminApplication.class, args);
        Key keyPad = new Key();

        String password = "0221";
        char[] pw = password.toCharArray();
        InputManager.getInputPage(keyPad);
        System.out.println("result hash: " + PasswordManager.savePassword(keyPad, pw));
        String result = ResultManager.getResult(PasswordManager.savePassword(keyPad, pw));
        ResultParser.parse(result);
    }
}
