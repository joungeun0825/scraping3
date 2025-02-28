package scraping.gookmin.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scraping.gookmin.dto.RequestDto;
import scraping.gookmin.dto.ResponseDto;
import scraping.gookmin.service.*;
import scraping.gookmin.util.Key;

@RestController
@RequestMapping("/api/account")
@AllArgsConstructor
public class AccountController {
    private final InputManager inputManager;
    private final PasswordManager passwordManager;
    private final ResultManager resultManager;

    @PostMapping
    public ResponseEntity<ResponseDto> getAccount(@RequestBody RequestDto requestBody) {
        Key keyPad = new Key();
        inputManager.getInputPage(keyPad);
        ResponseDto result = resultManager.getResult(passwordManager.savePassword(keyPad, requestBody), requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
