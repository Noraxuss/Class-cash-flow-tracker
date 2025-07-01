package cash_flow.dto.outgoing;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class GroupSelectionDetails extends SelectionParentClass {

    private String description;
}
