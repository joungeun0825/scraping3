package scraping.gookmin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import scraping.gookmin.dto.RequestDto;
import scraping.gookmin.util.ImageSlicer;
import scraping.gookmin.util.Key;

@RequiredArgsConstructor
@Service
public class PasswordManager {
    private final RestClient restClient;

    public String savePassword(Key keyPad, RequestDto requestDto) {
        ResponseEntity<byte[]> response2 = restClient.get()
                .uri(InputManager.getImageUrl())
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36")
                .header("Accept", "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8")
                .header("Accept-Encoding", "gzip, deflate, br, zstd")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Referer", "https://obank.kbstar.com/quics?page=C025255&cc=b028364:b028702&QSL=F")
                .header("Connection", "keep-alive")
                .header("Cookie", InputManager.getSessionCookie())
                .retrieve()
                .toEntity(byte[].class);  // 바디를 byte[]로 변환

        // 이미지 데이터 HEX 변환 및 저장
        if (response2.hasBody()) {
            return ImageSlicer.slice(response2.getBody(), keyPad, requestDto);
        }
        throw new IllegalArgumentException("이미지 데이터 없음.");
    }
}
