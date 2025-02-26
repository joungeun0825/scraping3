package scraping.gookmin.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "transaction_history")
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_date")
    private String transactionDate;

    @Column(name = "description")
    private String description;

    @Column(name = "account_content")
    private String accountContent;

    @Column(name = "withdrawal_amount")
    private String withdrawalAmount;

    @Column(name = "deposit_amount")
    private String depositAmount;

    @Column(name = "balance")
    private String balance;

    @Column(name = "transaction_branch")
    private String transactionBranch;

    @Column(name = "client")
    private String client;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public TransactionHistory(String transactionDate, String description, String accountContent, String withdrawalAmount, String depositAmount, String balance, String transactionBranch, Account account, String client) {
        this.transactionDate = transactionDate;
        this.description = description;
        this.accountContent = accountContent;
        this.withdrawalAmount = withdrawalAmount;
        this.depositAmount = depositAmount;
        this.balance = balance;
        this.transactionBranch = transactionBranch;
        this.account = account;
        this.client = client;
    }
}
