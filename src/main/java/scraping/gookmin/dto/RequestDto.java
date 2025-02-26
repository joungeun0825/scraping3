package scraping.gookmin.dto;

import lombok.Getter;

@Getter
public class RequestDto {
    private String password;
    private String accountNumber;
    private String userNumber;
    private String startDate;
    private String startYear;
    private String startMonth;
    private String startDay;
    private String endDate;
    private String endYear;
    private String endMonth;
    private String endDay;
    private String searchOption;
    private String checkOption;
    private String responseMethod;

    public RequestDto(String password, String accountNumber, String userNumber, String startDate, String endDate, String searchOption, String checkOption, String responseMethod) {
        this.password = password;
        this.accountNumber = accountNumber;
        this.userNumber = userNumber;
        this.startDate = startDate;
        this.startYear = startDate.substring(0,4);
        this.startMonth = startDate.substring(4,6);
        this.startDay = startDate.substring(6,8);
        this.endDate = endDate;
        this.endYear = endDate.substring(0,4);
        this.endMonth = endDate.substring(4,6);
        this.endDay = endDate.substring(6,8);
        this.searchOption = searchOption;
        this.checkOption = checkOption;
        this.responseMethod = responseMethod;
    }
}
