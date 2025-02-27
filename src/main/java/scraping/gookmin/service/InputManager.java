package scraping.gookmin.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import scraping.gookmin.util.CookieUtil;
import scraping.gookmin.util.Decoder;
import scraping.gookmin.util.Key;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class InputManager {
    private static final RestClient restClient = RestClient.builder()
            .baseUrl("https://obank.kbstar.com")
            .build();

    @Getter
    @Setter
    private static String sessionCookie;
    @Getter
    private static String userId;
    @Getter
    private static String name;
    @Getter
    private static String imageUrl;

    private static final String HEX2BIN_REGEX = "vKpd\\.hex2bin\\(\"(.*?)\"\\)";
    private static final String ID_REGEX = "'KEYPAD_USEYN_([a-zA-Z0-9]+)'";
    private static final String PASSWORD_REGEX = "\\.put\\('([^']+)'\\)";
    private static final String NAME_REGEX = "USEYN_CHECK_NAME_([a-zA-Z0-9]+)";
    private static final String SRC_REGEX = "src=\"([^\"]+)\"";

    public void getInputPage(Key keyPad) {
        ResponseEntity<String> response = requestInputPage();  // 응답 본문을 String으로 변환

        sessionCookie = CookieUtil.getCookie(response);
        getUserId(response);
        getName(response);
        String decodedJavascript = getPasswordJavascript(response);
        savePasswordKey(decodedJavascript, keyPad);
        getImageUrl(decodedJavascript);
    }

    private ResponseEntity<String> requestInputPage() {
        return restClient.get()
                .uri("/quics?page=C025255&cc=b028364:b028702&QSL=F")  // 상대경로만 작성
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .header("Accept-Encoding", "gzip, deflate, br, zstd")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Referer", "https://www.kbstar.com/")
                .header("Connection", "keep-alive")
                .retrieve()
                .toEntity(String.class);
    }

    private void getName(ResponseEntity<String> response) {
        Pattern pattern = Pattern.compile(NAME_REGEX);
        Matcher matcher = pattern.matcher(response.getBody());

        // 패턴이 매칭되는지 확인
        if (matcher.find()) {
            String extractedValue = matcher.group(1); // 캡처된 값 추출
            name = extractedValue;
        } else {
            throw new IllegalArgumentException("Cannot find USEYN_CHECK_NAME_ in the response body");
        }
    }

    private String getPasswordJavascript(ResponseEntity<String> response) {
        Pattern pattern = Pattern.compile(HEX2BIN_REGEX);
        Matcher matcher = pattern.matcher(response.getBody());
        if (matcher.find()) {
            String extractedValue = matcher.group(1);
            String decodedValue = Decoder.hexToString(extractedValue);
            return decodedValue;
        }
        throw new IllegalArgumentException("can not found vKpd.hex2bin()");
    }

    private void getImageUrl(String decodedJavascript) {
        Pattern pattern = Pattern.compile(SRC_REGEX);
        Matcher matcher = pattern.matcher(decodedJavascript);

        int count = 0;

        // 매칭된 모든 src 속성 반복
        while (matcher.find()) {
            count++;
            if (count == 2) {
                imageUrl = matcher.group(1).replace(";", "&");;  // 두 번째 값을 찾으면 저장
                break;
            }
        }

        if (imageUrl == null) {
            System.out.println("Second Image URL not found");
            throw new IllegalArgumentException("Can not found second image url");
        }
    }

    private void getUserId(ResponseEntity<String> response) {
        Pattern pattern2 = Pattern.compile(ID_REGEX);
        Matcher matcher2 = pattern2.matcher(response.getBody());

        while (matcher2.find()) {
            String extractedValue = matcher2.group(1);  // 작은 따옴표 포함된 전체 값
            userId = extractedValue;
        }
    }

    private void savePasswordKey(String response, Key keyPad) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(response);

        int count = 1;

        // 값 추출 후 Map에 저장
        while (matcher.find()) {
            String extractedValue = matcher.group(1); // 작은 따옴표 제외한 값
            keyPad.saveHashes(count++, extractedValue);
        }
    }
}
