package cash_flow.service;

import cash_flow.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public int getTotalPaymentsByMemberId(String id) {
        if (id == null || id.isEmpty()) {
            log.error("Member ID is null or empty, cannot retrieve total payments");
            return 0;
        }

        int totalPayments = paymentRepository.countByMemberId(id).orElse(0);
        log.info("Total payments for member ID {}: {}", id, totalPayments);
        return totalPayments;
    }
}
