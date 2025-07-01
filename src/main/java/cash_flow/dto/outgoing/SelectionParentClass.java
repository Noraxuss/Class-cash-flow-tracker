package cash_flow.dto.outgoing;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
public abstract class SelectionParentClass {

    private Long id;
    private String name;
}
