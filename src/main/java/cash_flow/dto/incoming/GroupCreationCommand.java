package cash_flow.dto.incoming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
public class GroupCreationCommand {

    private final String name;
    private final String description;
    private final String creationDateString;
    private final String endDateString;

}
