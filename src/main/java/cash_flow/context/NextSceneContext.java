package cash_flow.context;

import cash_flow.scene.SceneType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
@Setter
public class NextSceneContext {

    private SceneType sceneType;
}
