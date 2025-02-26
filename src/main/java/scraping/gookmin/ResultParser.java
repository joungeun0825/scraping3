package scraping.gookmin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ResultParser {
    public static void parse(String result) {
        Document document = Jsoup.parse(result);

        // 각 정보를 추출
        String accountInfo = document.select("tr.first td").text();  // 계좌정보
        String totalBalance = document.select("tr:nth-child(2) td:nth-child(2)").text();  // 총잔액
        String withdrawableBalance = document.select("tr:nth-child(2) td:nth-child(4)").text();  // 출금가능잔액
        String loanAmount = document.select("tr:nth-child(3) td:nth-child(2)").text();  // 대출약정금액
        String loanExpiryDate = document.select("tr:nth-child(3) td:nth-child(2)").text();  // 대출만료일 (대출약정금액과 같은 위치에 있음)
        String checkAmount = document.select("tr:nth-child(3) td:nth-child(4)").text();  // 자기앞수표금액, 가계수표금액, 기타타점권금액

        // 추출된 데이터 출력
        System.out.println("계좌정보: " + accountInfo);
        System.out.println("총잔액: " + totalBalance);
        System.out.println("출금가능잔액: " + withdrawableBalance);
        System.out.println("대출약정금액: " + loanAmount);
        System.out.println("대출만료일: " + loanExpiryDate);
        System.out.println("자기앞수표금액, 가계수표금액, 기타타점권금액: " + checkAmount);

        // 조회기간 추출
        String dateRange = document.select(".inq_btn_area p").text();  // "조회기간 : 2025.02.26 ~ 2025.02.26"

        // 거래내역 테이블의 거래 정보 추출
        Elements rows = document.select("table.tType01 tbody tr");
        for (Element row : rows) {
            String transactionDate = row.select("td:nth-child(1)").text();  // 거래일시
            String description = row.select("td:nth-child(2)").text();  // 적요
            String accountContent = row.select("td:nth-child(3)").text();  // 내통장표시내용
            String withdrawalAmount = row.select("td:nth-child(4)").text();  // 출금금액
            String depositAmount = row.select("td:nth-child(5)").text();  // 입금금액
            String balance = row.select("td:nth-child(6)").text();  // 잔액
            String transactionBranch = row.select("td:nth-child(7)").text();  // 거래점

            // 추출된 정보 출력
            System.out.println("거래일시: " + transactionDate);
            System.out.println("적요: " + description);
            System.out.println("내통장표시내용: " + accountContent);
            System.out.println("출금금액: " + withdrawalAmount);
            System.out.println("입금금액: " + depositAmount);
            System.out.println("잔액: " + balance);
            System.out.println("거래점: " + transactionBranch);
            System.out.println("===================================");
        }

        // 조회기간 출력
        System.out.println("조회기간: " + dateRange);
    }
}
