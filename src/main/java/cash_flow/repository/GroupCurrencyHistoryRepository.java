package cash_flow.repository;

import cash_flow.domain.GroupCurrencyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupCurrencyHistoryRepository extends JpaRepository<GroupCurrencyHistory,Long> {
}
