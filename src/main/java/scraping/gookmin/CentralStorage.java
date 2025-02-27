package scraping.gookmin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class CentralStorage {
    @Getter
    private static final Map<String, List<AccountInfo>> centralUserInfo = new HashMap<>();

    public static void saveUserInfo(String userId, AccountInfo accountInfo) {
        if(!centralUserInfo.containsKey(userId)){
            centralUserInfo.put(userId, new ArrayList<>());
        }
        centralUserInfo.get(userId).add(accountInfo);
    }

    public static boolean containsKey(String userId){
        return centralUserInfo.containsKey(userId);
    }

    public static void remove(String userId){
        centralUserInfo.remove(userId);
    }

    public static MultiValueMap<String, String> getInfo(String userId) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        AccountInfo accountInfo = centralUserInfo.get(userId).getLast();

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
}
