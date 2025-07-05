package cash_flow.repository;

import cash_flow.domain.Guardian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuardianRepository extends JpaRepository<Guardian, String> {
}
