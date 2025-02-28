package scraping.gookmin.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import scraping.gookmin.domain.Account;
import scraping.gookmin.domain.TransactionHistory;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ResultParser {

    public String getDateRange(String result) {
        Document document = Jsoup.parse(result);
        return document.select(".inq_btn_area p").text().split(" : ")[1];
    }

    public Account parseAccount(String result) {
        Document document = Jsoup.parse(result);
        // 각 정보를 추출
        String accountInfo = document.select("tr.first td").text().split(" ")[0];  // 계좌정보
        String totalBalance = document.select("tr:nth-child(2) td:nth-child(2)").text().split(" ")[0];   // 총잔액
        String withdrawableBalance = document.select("tr:nth-child(2) td:nth-child(4)").text();  // 출금가능잔액
        String loanAmount = document.select("tr:nth-child(3) td:nth-child(2)").text().split(" ")[0];   // 대출약정금액
        String loanExpiryDate = document.select("tr:nth-child(3) td:nth-child(2)").text().split(" ")[0];   // 대출만료일 (대출약정금액과 같은 위치에 있음)
        String checkAmount = document.select("tr:nth-child(3) td:nth-child(4)").text().split(" ")[0];   // 자기앞수표금액, 가계수표금액, 기타타점권금액

        return new Account(accountInfo, totalBalance, withdrawableBalance, loanAmount, loanExpiryDate, checkAmount);
    }

    public List<TransactionHistory> parseTransactionHistory(List<TransactionHistory> histories, String result) {
        Document document = Jsoup.parse(result);
        // 거래내역 테이블의 거래 정보 추출
        Elements rows = document.select("table.tType01 tbody tr");
        for (int i = 0; i < rows.size(); i++) {
            if (i % 2 == 1) continue;
            Element row = rows.get(i);
            String transactionDate = row.select("td:nth-child(1)").text();  // 거래일시
            String description = row.select("td:nth-child(2)").text();  // 적요
            String accountContent = row.select("td:nth-child(3)").text();  // 내통장표시내용
            String withdrawalAmount = row.select("td:nth-child(4)").text();  // 출금금액
            String depositAmount = row.select("td:nth-child(5)").text();  // 입금금액
            String balance = row.select("td:nth-child(6)").text();  // 잔액
            String transactionBranch = row.select("td:nth-child(7)").text();
            String client = "";

            if (i + 1 < rows.size()) {
                Element nextRow = rows.get(i + 1);
                client = nextRow.select("td:nth-child(1)").text();  // 두 번째 행의 첫 번째 열 (박정은)
            }
            TransactionHistory transactionHistory = new TransactionHistory(transactionDate, description, accountContent, withdrawalAmount, depositAmount, balance, transactionBranch, client);
            histories.add(transactionHistory);
        }
        return histories;
    }
}
