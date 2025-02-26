package scraping.gookmin.service;

import gnu.cajo.invoke.JClient;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import scraping.gookmin.domain.Account;
import scraping.gookmin.domain.TransactionHistory;
import scraping.gookmin.dto.ResponseDto;
import scraping.gookmin.repository.AccountRepository;
import scraping.gookmin.repository.TransactionHistoryRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ResultParser {
    private final AccountRepository accountRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

    public ResponseDto parse(String result) {
        Document document = Jsoup.parse(result);
        Account account = parseAccount(document);
        List<TransactionHistory> histories = parseTransactionHistory(document, account);
        String dateRange = document.select(".inq_btn_area p").text().split(" : ")[1];   // "조회기간 : 2025.02.26 ~ 2025.02.26"
        return new ResponseDto(dateRange, account, histories);
    }

    private Account parseAccount(Document document) {
        // 각 정보를 추출
        String accountInfo = document.select("tr.first td").text().split(" ")[0];  // 계좌정보
        String totalBalance = document.select("tr:nth-child(2) td:nth-child(2)").text().split(" ")[0];   // 총잔액
        String withdrawableBalance = document.select("tr:nth-child(2) td:nth-child(4)").text();  // 출금가능잔액
        String loanAmount = document.select("tr:nth-child(3) td:nth-child(2)").text().split(" ")[0];   // 대출약정금액
        String loanExpiryDate = document.select("tr:nth-child(3) td:nth-child(2)").text().split(" ")[0];   // 대출만료일 (대출약정금액과 같은 위치에 있음)
        String checkAmount = document.select("tr:nth-child(3) td:nth-child(4)").text().split(" ")[0];   // 자기앞수표금액, 가계수표금액, 기타타점권금액

        return accountRepository.save(new Account(accountInfo, totalBalance, withdrawableBalance, loanAmount, loanExpiryDate, checkAmount));
    }

    private List<TransactionHistory> parseTransactionHistory(Document document, Account account) {
        List<TransactionHistory> histories = new ArrayList<>();

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
            TransactionHistory transactionHistory = transactionHistoryRepository.save(new TransactionHistory(transactionDate, description, accountContent, withdrawalAmount, depositAmount, balance, transactionBranch, account, client));
            histories.add(transactionHistory);
        }
        return histories;
    }
}
