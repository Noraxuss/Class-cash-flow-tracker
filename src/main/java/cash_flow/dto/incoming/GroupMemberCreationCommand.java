package cash_flow.dto.incoming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GroupMemberCreationCommand {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final Long guardianId;

}
