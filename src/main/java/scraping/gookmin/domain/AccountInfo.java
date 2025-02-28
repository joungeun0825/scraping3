package scraping.gookmin.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountInfo {
    private String lastReqTime;
    private String signedMsg;
    private String reqKey;
    private String accountNumber;
    private String startDate;
    private String endDate;
    private String businessNumber;
    private String userNumber;
    private String fastSearch;
    private String sPageKeyValue;
    private String sPresentNum;
    private String nextDateKey;
    private String nextSerialNumKey;
    private String beforeNextDateKey;
    private String beforeNextSerialNumKey;
    private String startYear;
    private String startMonth;
    private String startDay;
    private String endYear;
    private String endMonth;
    private String endDay;
}
