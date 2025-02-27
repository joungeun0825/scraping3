package scraping.gookmin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import scraping.gookmin.FormDataExtractor;
import scraping.gookmin.dto.RequestDto;

@RequiredArgsConstructor
@Service
public class ResultManager {
    private final RestClient restClient = RestClient.builder()
            .baseUrl("https://obank.kbstar.com")
            .build();

    public String getResult(String hash, RequestDto requestDto) {
        MultiValueMap<String, String> formData = getFormData(hash, requestDto);

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
                .toEntity(String.class);

        if (response2.hasBody()) {
            FormDataExtractor.extractAndSaveFormData(requestDto.getUserNumber(), response2.getBody(), null);
            return response2.getBody();
        }
        throw new IllegalArgumentException("Invalid Result");
    }

    private MultiValueMap<String, String> getFormData(String hash, RequestDto requestDto) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(String.format("KEYPAD_TYPE_%s", InputManager.getUserId()), "3");
        formData.add(String.format("KEYPAD_HASH_%s", InputManager.getUserId()), hash);
        formData.add(String.format("KEYPAD_USEYN_%s", InputManager.getUserId()), String.format("USEYN_CHECK_NAME_%s", InputManager.getName()));
        formData.add(String.format("KEYPAD_INPUT_%s", InputManager.getUserId()), "비밀번호");
        formData.add("signed_msg", "");
        formData.add("요청키", "");
        formData.add("계좌번호", requestDto.getAccountNumber());
        formData.add("조회시작일자", requestDto.getStartDate());
        formData.add("조회종료일", requestDto.getEndDate());
        formData.add("고객식별번호", "");
        formData.add("빠른조회", "Y");
        formData.add("조회계좌", requestDto.getAccountNumber());
        formData.add("비밀번호", requestDto.getPassword());
        formData.add(String.format("USEYN_CHECK_NAME_", InputManager.getName()), "Y");
        formData.add("검색구분", "2");
        formData.add("주민사업자번호", requestDto.getUserNumber());
        formData.add("조회시작년", requestDto.getStartYear());
        formData.add("조회시작월", requestDto.getStartMonth());
        formData.add("조회시작일", requestDto.getStartDay());
        formData.add("조회끝년", requestDto.getEndYear());
        formData.add("조회끝월", requestDto.getEndMonth());
        formData.add("조회끝일", requestDto.getEndDay());
        formData.add("조회구분", requestDto.getCheckOption());
        formData.add("응답방법", requestDto.getResponseMethod());
        return formData;
    }
}
