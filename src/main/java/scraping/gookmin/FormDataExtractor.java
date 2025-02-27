package scraping.gookmin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormDataExtractor {
    private static final String STR_REQ_KEY1 = "var\\s+strReqKey1\\s*=\\s*[\"'](.*?)[\"'];";
    private static final String STR_REQ_KEY2 = "var\\s+strReqKey2\\s*=\\s*[\"'](.*?)[\"'];";
    private static final String STR_BACK_KEY1 = "var\\s+strBackKey1\\s*=\\s*[\"'](.*?)[\"'];";
    private static final String STR_BACK_KEY2 = "var\\s+strBackKey2\\s*=\\s*[\"'](.*?)[\"'];";
    private static final String STR_NEXT_YM = "var\\s+strNextYM\\s*=\\s*[\"'](.*?)[\"'];";
    private static final String STR_NEXT_SEQ = "var\\s+strNextSeq\\s*=\\s*[\"'](.*?)[\"'];";

    public static void extractAndSaveFormData(String userId, String result) {
        System.out.println(result);
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
            CentralStorage.saveUserInfo(userId, accountInfo);

        } else {
            System.out.println("Form not found!");
        }
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
            System.out.println("Value: " + value);
            return value;
        }
        throw new IllegalArgumentException("cannot find value");
    }

    private static MultiValueMap<String, String> extractFormData(Element form) {
        Elements inputs = form.select("input");
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        for (Element input : inputs) {
            String name = input.attr("name");
            String value = input.attr("value");
            formData.add(name, value);
        }
        return formData;
    }
}
