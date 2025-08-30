package cash_flow.dto.incoming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GroupCreationCommand {

    private final String name;
    private final String description;
    private final String creationDateString;
    private final String endDateString;
    private final String overseerId;
    private final String currencyCode;

}
