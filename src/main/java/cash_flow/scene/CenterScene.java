package cash_flow.scene;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class CenterScene extends BaseScene {

    private static CenterScene instance;

    private CenterScene() {}
}
