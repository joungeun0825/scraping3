package scraping.gookmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scraping.gookmin.domain.TransactionHistory;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
}
