package scraping.gookmin.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Account {

    private String accountNumber;
    private String totalBalance;
    private String withDrawableBalance;
    private String loanAmount;
    private String loanExpiryDate;
    private String checkAmount;

    public Account(String accountNumber, String totalBalance, String withDrawableBalance, String loanAmount, String loanExpiryDate, String checkAmount){
        this.accountNumber = accountNumber;
        this.totalBalance = totalBalance;
        this.withDrawableBalance = withDrawableBalance;
        this.loanAmount = loanAmount;
        this.loanExpiryDate = loanExpiryDate;
        this.checkAmount = checkAmount;
    }

}
