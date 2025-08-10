package cash_flow.service;

import cash_flow.common.StatusResponses;
import cash_flow.context.AppContext;
import cash_flow.domain.Overseer;
import cash_flow.domain.PersonType;
import cash_flow.dto.incoming.OverseerCreationCommand;
import cash_flow.dto.outgoing.OverseerSelectionDetails;
import cash_flow.repository.OverseerRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OverseerService {

    private final OverseerRepository overseerRepository;
    private final ModelMapper modelMapper;
    private final PersonService personService;
    private final AppContext appContext;

    @Autowired
    public OverseerService(OverseerRepository overseerRepository, ModelMapper modelMapper, PersonService personService, AppContext appContext) {
        this.overseerRepository = overseerRepository;
        this.modelMapper = modelMapper;
        this.personService = personService;
        this.appContext = appContext;
    }

    public StatusResponses createNewOverseer(OverseerCreationCommand command) {
        // Check if the command is null
        if (command.getFirstName() == null || command.getLastName() == null) {
            log.error("Overseer creation failed: First name or last name is null");
            return StatusResponses.FAILURE;
        }
        // Check if an overseer with the same first and last name already exists
        if (overseerRepository.existsByFirstNameAndLastNameAndEmail(
                command.getFirstName(),
                command.getLastName(),
                command.getEmail())) {
            log.error("Overseer creation failed: Overseer with name {} {} already exists", command.getFirstName(), command.getLastName());
            return StatusResponses.ALREADY_EXISTS;
        }
        // Map the command to an Overseer entity and set additional properties
        Overseer overseer = modelMapper.map(command, Overseer.class);

        // Set the person type to Overseer
        PersonType personType = PersonType.OVERSEER;
        overseer.setPersonType(personType);

        // Generate a unique ID for the overseer
        Long idNumber = overseerRepository.count() + 1;
        String id = personService.createPersonId(overseer, idNumber);
        overseer.setId(id);

        // Set the date of creation to the current date and time
        overseer.setDateOfCreation(LocalDateTime.now());

        // Save the overseer entity to the repository
        overseerRepository.save(overseer);
        log.info("New overseer created: {} {}", overseer.getFirstName(), overseer.getLastName());
        appContext.getOverseerContext().setOverseerId(id);
        return StatusResponses.SUCCESS;
    }

    public List<OverseerSelectionDetails> getOverSeerList() {
        // Fetch all overseers from the repository
        List<Overseer> overseerList = overseerRepository.findAll();

        // Map the list of Overseer entities to a list of OverseerSelectionDetails DTOs
        List<OverseerSelectionDetails> overseerSelectionDetails = new ArrayList<>();
        overseerList.forEach(overseer -> {
            OverseerSelectionDetails details = (OverseerSelectionDetails)
                    personService.createSelectionDetails(overseer);
            if (details != null) {
                overseerSelectionDetails.add(details);
            }
        });
        return overseerSelectionDetails;
    }

    public OverseerSelectionDetails getOverSeerDetails(String id) {
        // Retrieve the overseer by ID
        Overseer overseer = overseerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Overseer with ID " + id + " not found"));

        // Map the Overseer entity to OverseerSelectionDetails DTO
        return (OverseerSelectionDetails) personService.createSelectionDetails(overseer);
    }


    /**
     * Retrieves an overseer by their ID.
     *
     * @param id the ID of the overseer to retrieve
     * @return the Overseer object if found
     * @throws IllegalArgumentException if no overseer with the given ID exists
     */
    public Overseer getOverseerById(String id) {
        return overseerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Overseer with ID " + id + " not found"));
    }
}
