package cash_flow.service;

import cash_flow.domain.Overseer;
import cash_flow.dto.outgoing.OverseerSelectionDetails;
import cash_flow.dto.outgoing.SelectionParentClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class PersonService {

    public String createPersonId(Overseer overseer, Long idNumber) {
        StringBuilder personId = new StringBuilder();
        if (overseer == null || overseer.getPersonType() == null || overseer.getFirstName() == null || overseer.getLastName() == null) {
            log.error("Invalid overseer data provided for ID generation.");
            return null;
        }
        personId.append(overseer.getPersonType().getTypeName());
        personId.append("-");
        personId.append(overseer.getFirstName().charAt(0));
        personId.append(overseer.getLastName().charAt(0));
        personId.append("-");
        personId.append(idNumber);
        personId.append("-");
        personId.append(LocalDate.now().getYear());
        personId.append(LocalDate.now().getMonthValue());
        personId.append(LocalDate.now().getDayOfMonth());
        return personId.toString();
    }

    public SelectionParentClass createSelectionDetails(Overseer overseer) {
        if (overseer == null) {
            log.error("Overseer is null, cannot create selection details.");
            return null;
        }
        SelectionParentClass selectionDetails = new OverseerSelectionDetails();
        selectionDetails.setId(overseer.getId());
        selectionDetails.setName(overseer.getFirstName() + " " + overseer.getLastName());
        return selectionDetails;
    }

}
