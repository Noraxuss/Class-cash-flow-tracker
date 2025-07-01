package cash_flow.repository;

import cash_flow.domain.RequiredPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequiredPaymentRepository extends JpaRepository<RequiredPayment, Long> {
    // Additional query methods can be defined here if needed
}
