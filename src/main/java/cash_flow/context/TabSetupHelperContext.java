package cash_flow.context;

import javafx.scene.control.Tab;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
@Getter
@Setter
public class TabSetupHelperContext {

    private final Set<Tab> treatedTabs = new HashSet<>();
}
