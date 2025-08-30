package cash_flow.repository;

import cash_flow.domain.Logs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Logs, String> {
}
