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

    private EnumMap<SceneType, Stage> extraStages = new EnumMap<>(SceneType.class);

    /**
     * Adds a new stage for the given scene type.
     *
     * @param sceneType the type of scene for which the stage is being added
     * @param stage     the stage to be added
     */
    public void addStage(SceneType sceneType, Stage stage) {
        log.info("Adding stage for scene type: {}", sceneType);
        extraStages.put(sceneType, stage);
    }

}
