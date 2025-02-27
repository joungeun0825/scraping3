package scraping.gookmin.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "total_balance")
    private String totalBalance;

    @Column(name = "with_drawable_balance")
    private String withDrawableBalance;

    @Column(name = "loan_amount")
    private String loanAmount;

    @Column(name = "loan_expiry_date")
    private String loanExpiryDate;

    @Column(name = "check_amount")
    private String checkAmount;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account", orphanRemoval = true)
    private List<TransactionHistory> transactionHistories;

    public Account(String accountNumber, String totalBalance, String withDrawableBalance, String loanAmount, String loanExpiryDate, String checkAmount){
        this.accountNumber = accountNumber;
        this.totalBalance = totalBalance;
        this.withDrawableBalance = withDrawableBalance;
        this.loanAmount = loanAmount;
        this.loanExpiryDate = loanExpiryDate;
        this.checkAmount = checkAmount;
    }

}
