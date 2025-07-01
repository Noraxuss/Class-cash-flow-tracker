package cash_flow.service;

import cash_flow.repository.RequiredPaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RequiredPaymentService {

    private final RequiredPaymentRepository requiredPaymentRepository;

    public RequiredPaymentService(RequiredPaymentRepository requiredPaymentRepository) {
        this.requiredPaymentRepository = requiredPaymentRepository;
    }
}
