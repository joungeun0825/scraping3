package scraping.gookmin.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransactionHistory {
    private String transactionDate;
    private String description;
    private String accountContent;
    private String withdrawalAmount;
    private String depositAmount;
    private String balance;
    private String transactionBranch;
    private String client;

    public TransactionHistory(String transactionDate, String description, String accountContent, String withdrawalAmount, String depositAmount, String balance, String transactionBranch, String client) {
        this.transactionDate = transactionDate;
        this.description = description;
        this.accountContent = accountContent;
        this.withdrawalAmount = withdrawalAmount;
        this.depositAmount = depositAmount;
        this.balance = balance;
        this.transactionBranch = transactionBranch;
        this.client = client;
    }
}
