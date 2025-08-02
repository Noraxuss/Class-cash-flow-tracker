 package cash_flow.service;

import cash_flow.controller.LoadingController;
import cash_flow.domain.Guardian;
import cash_flow.domain.PersonType;
import cash_flow.dto.incoming.GuardianCreationCommand;
import cash_flow.repository.GuardianRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class GuardianService {

    private final GuardianRepository guardianRepository;
    private final ModelMapper modelMapper;
    private final LoadingController loadingController;
    private final PersonService personService;

    @Autowired
    public GuardianService(GuardianRepository guardianRepository, ModelMapper modelMapper, LoadingController loadingController, PersonService personService) {
        this.guardianRepository = guardianRepository;
        this.modelMapper = modelMapper;
        this.loadingController = loadingController;
        this.personService = personService;
    }

    public String createGuardian(GuardianCreationCommand guardianCreationCommand) {
        if (guardianCreationCommand == null) {
            log.error("Guardian creation command is null");
            return "Guardian creation command cannot be null";
        }

        String currentStep = "Creating Guardian %s %s"
                .formatted(guardianCreationCommand.getFirstName(), guardianCreationCommand.getLastName());
        loadingController.updateCurrentStepLabel(currentStep);

        Optional<Guardian> existingGuardian = guardianRepository.findByEmailAndFirstNameAndLastName(
                guardianCreationCommand.getEmail(),
                guardianCreationCommand.getFirstName(),
                guardianCreationCommand.getLastName()
        );

        if (existingGuardian.isPresent()) {
            log.warn("Guardian with email {} already exists", guardianCreationCommand.getEmail());
            return existingGuardian.get().getId();
        }

        Guardian guardian = modelMapper.map(guardianCreationCommand, Guardian.class);
        guardian.setPersonType(PersonType.GUARDIAN);
        guardian.setDateOfCreation(LocalDateTime.now());
        guardian.setId(personService.createPersonId(guardian, guardianRepository.count() + 1));
        guardianRepository.save(guardian);

        log.info("Guardian created with ID: {}", guardian.getId());
        return guardian.getId();
    }

    public Guardian getGuardian(String guardianId) {
        return guardianRepository.findById(guardianId).orElse(null);
    }
}
