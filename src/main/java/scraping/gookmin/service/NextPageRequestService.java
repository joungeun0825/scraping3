package scraping.gookmin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import scraping.gookmin.CentralStorage;
import scraping.gookmin.FormDataExtractor;
import scraping.gookmin.util.CookieUtil;

@RequiredArgsConstructor
@Service
public class NextPageRequestService {
    private final RestClient restClient = RestClient.builder()
            .baseUrl("https://obank.kbstar.com")
            .build();

    public String getNextPage(String userId) {
        MultiValueMap<String, String> formData2 = CentralStorage.getInfo(userId);
        ResponseEntity<String> response2 = restClient.post()
                .uri("/quics?chgCompId=b028770&baseCompId=b028702&page=C025255&cc=b028702:b028770")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36")
                .header("Accept", "text/html, */*")
                .header("Accept-Encoding", "gzip, deflate, br, zstd")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Referer", "https://obank.kbstar.com/quics?page=C025255&cc=b028364:b028702&QSL=F")
                .header("Connection", "keep-alive")
                .header("Cookie", InputManager.getSessionCookie())
                .body(formData2)
                .retrieve()
                .toEntity(String.class);

        if (response2.hasBody()) {
            FormDataExtractor.extractAndSaveFormData(InputManager.getUserId(), response2.getBody());
            return response2.getBody();
        }
        throw new IllegalArgumentException("Invalid Result");
    }
}
