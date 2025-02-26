package scraping.gookmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scraping.gookmin.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long>  {
}
