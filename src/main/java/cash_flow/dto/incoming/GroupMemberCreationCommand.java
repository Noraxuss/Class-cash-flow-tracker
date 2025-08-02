package cash_flow.dto.incoming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class GroupMemberCreationCommand {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final LocalDate startDate;
    private final String guardianId;

}
