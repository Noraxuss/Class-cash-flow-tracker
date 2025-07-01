package cash_flow.service;

import cash_flow.repository.GuardianRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GuardianService {

    private final GuardianRepository guardianRepository;

    @Autowired
    public GuardianService(GuardianRepository guardianRepository) {
        this.guardianRepository = guardianRepository;
    }
}
