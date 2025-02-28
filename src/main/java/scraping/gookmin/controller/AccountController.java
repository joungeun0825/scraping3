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
    private final InfoExtractor infoExtractor;
    private final PasswordManager passwordManager;
    private final ResultManager resultManager;

    @PostMapping
    public ResponseEntity<ResponseDto> getAccount(@RequestBody RequestDto requestBody, @RequestParam int page) {
        Key keyPad = new Key();
        infoExtractor.extract(keyPad);
        String password = passwordManager.savePassword(keyPad, requestBody);
        ResponseDto result = resultManager.getResult(password, requestBody, page);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
