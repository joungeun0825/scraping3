package scraping.gookmin.dto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import scraping.gookmin.domain.TransactionHistory;

@Getter
@Setter
@ToString
public class HistoryDto {
    private String transactionDate;
    private String description;
    private String accountContent;
    private String withdrawalAmount;
    private String depositAmount;
    private String balance;
    private String transactionBranch;
    private String client;

    public HistoryDto(TransactionHistory  transactionHistory) {
        this.transactionDate = transactionHistory.getTransactionDate();
        this.description = transactionHistory.getDescription();
        this.accountContent = transactionHistory.getAccountContent();
        this.withdrawalAmount = transactionHistory.getWithdrawalAmount();
        this.depositAmount = transactionHistory.getDepositAmount();
        this.balance = transactionHistory.getBalance();
        this.transactionBranch = transactionHistory.getTransactionBranch();
        this.client = transactionHistory.getClient();
    }
}
