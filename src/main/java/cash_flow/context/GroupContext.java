package cash_flow.context;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
@Getter
@Setter
public class GroupContext {

    private Long groupId;
    private LocalDate startDate;

}
