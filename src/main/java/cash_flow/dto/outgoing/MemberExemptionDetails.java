package cash_flow.dto.outgoing;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberExemptionDetails {

    private String id;
    private String name;
    private boolean isExempted;

}
