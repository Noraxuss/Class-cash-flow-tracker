package cash_flow.scene;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class SideBarScene extends BaseScene{

    private static SideBarScene instance;

    private SideBarScene() {}
}
