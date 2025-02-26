package scraping.gookmin.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scraping.gookmin.dto.RequestDto;
import scraping.gookmin.dto.ResponseDto;
import scraping.gookmin.service.InputManager;
import scraping.gookmin.service.PasswordManager;
import scraping.gookmin.service.ResultManager;
import scraping.gookmin.service.ResultParser;
import scraping.gookmin.util.Key;

@RestController
@RequestMapping("/api/account")
@AllArgsConstructor
public class AccountController {
    private final InputManager inputManager;
    private final PasswordManager passwordManager;
    private final ResultManager resultManager;
    private final ResultParser resultParser;

    @PostMapping
    public ResponseEntity<ResponseDto> getOrderList(@RequestBody RequestDto requestBody) {
        Key keyPad = new Key();
        inputManager.getInputPage(keyPad);
        String result = resultManager.getResult(passwordManager.savePassword(keyPad, requestBody), requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultParser.parse(result));
    }
}
