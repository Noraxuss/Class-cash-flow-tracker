package cash_flow.context;

import cash_flow.scene.SceneType;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.EnumMap;

@Component
@Slf4j
@Getter
@Setter
public class StageContext {

    private EnumMap<SceneType, Stage> scenes = new EnumMap<>(SceneType.class);

}
