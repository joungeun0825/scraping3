package scraping.gookmin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import scraping.gookmin.domain.Account;
import scraping.gookmin.domain.TransactionHistory;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class ResponseDto {
    private Long id;
    private String dateRange;
    private String accountInfo;
    private String totalBalance;
    private String withDrawableBalance;
    private String loanAmount;
    private String loanExpiryDate;
    private String checkAmount;
    private List<HistoryDto> transactionHistories = new ArrayList<>();

    public ResponseDto(String dateRange, Account account, List<TransactionHistory> histories) {
        this.id = account.getId();
        this.dateRange = dateRange;
        this.accountInfo = account.getAccountInfo();
        this.totalBalance = account.getTotalBalance();
        this.withDrawableBalance = account.getWithDrawableBalance();
        this.loanAmount = account.getLoanAmount();
        this.loanExpiryDate = account.getLoanExpiryDate();
        this.checkAmount = account.getCheckAmount();
        for(TransactionHistory history : histories) {
            transactionHistories.add(new HistoryDto(history));
        }
    }
}
