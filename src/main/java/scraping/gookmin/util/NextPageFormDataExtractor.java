package scraping.gookmin.util;

import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import scraping.gookmin.domain.AccountInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NextPageFormDataExtractor {
    @Getter
    private static String nextSeq;

    private static final String STR_REQ_KEY1 = "var\\s+strReqKey1\\s*=\\s*[\"'](.*?)[\"'];";
    private static final String STR_REQ_KEY2 = "var\\s+strReqKey2\\s*=\\s*[\"'](.*?)[\"'];";
    private static final String STR_BACK_KEY1 = "var\\s+strBackKey1\\s*=\\s*[\"'](.*?)[\"'];";
    private static final String STR_BACK_KEY2 = "var\\s+strBackKey2\\s*=\\s*[\"'](.*?)[\"'];";
    private static final String STR_NEXT_YM = "var\\s+strNextYM\\s*=\\s*[\"'](.*?)[\"'];";
    private static final String STR_NEXT_SEQ = "var\\s+strNextSeq\\s*=\\s*[\"'](.*?)[\"'];";

    public static MultiValueMap<String, String> extractAndSaveFormData(String userId, String result) {
        Document doc = Jsoup.parse(result);
        Element form = doc.selectFirst("form");
        Elements scripts = doc.select("script");
        if (form != null) {
            Elements inputs = form.select("input");
            AccountInfo accountInfo = new AccountInfo(
                    getInputValue(inputs, "lastReqTime"),
                    getInputValue(inputs, "signed_msg"),
                    getInputValue(inputs, "요청키"),
                    getInputValue(inputs, "계좌번호"),
                    getInputValue(inputs, "조회시작일자"),
                    getInputValue(inputs, "조회종료일"),
                    getInputValue(inputs, "주민사업자번호"),
                    getInputValue(inputs, "고객식별번호"),
                    getInputValue(inputs, "빠른조회"),
                    getInputValue(inputs, "s_pageKeyValue"),
                    getInputValue(inputs, "s_presentNum"),
                    getValue(STR_NEXT_YM, scripts),
                    getValue(STR_NEXT_SEQ, scripts),
                    getValue(STR_BACK_KEY1, scripts) + "|" + getValue(STR_REQ_KEY1, scripts),
                    getValue(STR_BACK_KEY2, scripts) + "|" + getValue(STR_REQ_KEY2, scripts),
                    getInputValue(inputs, "조회시작년"),
                    getInputValue(inputs, "조회시작월"),
                    getInputValue(inputs, "조회시작일"),
                    getInputValue(inputs, "조회끝년"),
                    getInputValue(inputs, "조회끝월"),
                    getInputValue(inputs, "조회끝일")
            );
            nextSeq = accountInfo.getNextSerialNumKey();
            return getInfo(userId, accountInfo);

        }
        throw new IllegalArgumentException("Form not found!");
    }

    public static MultiValueMap<String, String> getInfo(String userId, AccountInfo accountInfo) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("lastReqTime", accountInfo.getLastReqTime());
        formData.add("signed_msg", accountInfo.getSignedMsg());
        formData.add("요청키", accountInfo.getReqKey());
        formData.add("계좌번호", accountInfo.getAccountNumber());
        formData.add("조회시작일자", accountInfo.getStartYear()+accountInfo.getStartMonth()+accountInfo.getStartDay());
        formData.add("조회종료일", accountInfo.getEndYear()+accountInfo.getEndMonth()+accountInfo.getEndDay());
        formData.add("주민사업자번호", userId);
        formData.add("고객식별번호", accountInfo.getUserNumber());
        formData.add("빠른조회", "Y");
        formData.add("s_pageKeyValue", accountInfo.getSPageKeyValue());
        formData.add("s_presentNum", accountInfo.getSPresentNum());
        formData.add("다음거래년월일키", accountInfo.getNextDateKey());
        formData.add("다음거래일련번호키", accountInfo.getNextSerialNumKey());
        formData.add("이전다음거래년월일키", accountInfo.getBeforeNextDateKey());
        formData.add("이전다음거래일련번호키", accountInfo.getBeforeNextSerialNumKey());
        formData.add("조회시작년", accountInfo.getStartYear());
        formData.add("조회시작월", accountInfo.getStartMonth());
        formData.add("조회시작일", accountInfo.getStartDay());
        formData.add("조회끝년", accountInfo.getEndYear());
        formData.add("조회끝월", accountInfo.getEndMonth());
        formData.add("조회끝일", accountInfo.getEndDay());
        formData.add("조회구분", "2");
        formData.add("응답방법", "2");
        return formData;
    }

    private static String getInputValue(Elements inputs, String name) {
        for (Element input : inputs) {
            if (input.attr("name").equals(name)) {
                return input.attr("value");
            }
        }
        return ""; // 값이 없으면 빈 문자열 반환
    }

    private static String getValue(String regex, Elements scripts) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(scripts.html());
        if (matcher.find()) {
            String value = matcher.group(1);
            return value;
        }
        throw new IllegalArgumentException("cannot find value");
    }
}
