package cash_flow.repository;

import cash_flow.domain.Currency;
import cash_flow.domain.GroupMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency,String> {
}
