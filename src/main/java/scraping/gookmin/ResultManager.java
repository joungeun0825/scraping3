package scraping.gookmin;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

public class ResultManager {
    private static final RestClient restClient = RestClient.builder()
            .baseUrl("https://obank.kbstar.com")
            .build();

    public static String getResult(String hash) {
        MultiValueMap<String, String> formData = getFormData(hash);

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
                .body(formData)
                .retrieve()
                .toEntity(String.class);  // 바디를 byte[]로 변환


        if (response2.hasBody()) {
            return response2.getBody();
        }
        throw new IllegalArgumentException("Invalid Result");
    }

    private static MultiValueMap<String, String> getFormData(String hash) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(String.format("KEYPAD_TYPE_%s", InputManager.getUserId()), "3");
        formData.add(String.format("KEYPAD_HASH_%s", InputManager.getUserId()), hash);
        formData.add(String.format("KEYPAD_USEYN_%s", InputManager.getUserId()), String.format("USEYN_CHECK_NAME_%s", InputManager.getName()));
        formData.add(String.format("KEYPAD_INPUT_%s", InputManager.getUserId()), "비밀번호");
        formData.add("signed_msg", "");
        formData.add("요청키", "");
        formData.add("계좌번호", "92900201279988");
        formData.add("조회시작일자", "20250226");
        formData.add("조회종료일", "20250226");
        formData.add("고객식별번호", "");
        formData.add("빠른조회", "Y");
        formData.add("조회계좌", "92900201279988");
        formData.add("비밀번호", "0221");
        formData.add(String.format("USEYN_CHECK_NAME_", InputManager.getName()), "Y");
        formData.add("검색구분", "2");
        formData.add("주민사업자번호", "010825");
        formData.add("조회시작년", "2025");
        formData.add("조회시작월", "02");
        formData.add("조회시작일", "26");
        formData.add("조회끝년", "2025");
        formData.add("조회끝월", "02");
        formData.add("조회끝일", "26");
        formData.add("조회구분", "2");
        formData.add("응답방법", "2");
        return formData;
    }
}
