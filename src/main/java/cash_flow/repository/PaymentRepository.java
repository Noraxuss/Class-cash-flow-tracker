package cash_flow.repository;

import cash_flow.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {

    @Query(value = "SELECT COUNT(p) FROM Payment p WHERE p.member.id = :id")
    Optional<Integer> countByMemberId(@Param("id") String id);
}
