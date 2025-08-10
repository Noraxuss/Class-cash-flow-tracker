package cash_flow.controller.utilities;

import cash_flow.scene.SceneConfigurationLoader;
import cash_flow.scene.SceneEngine;
import javafx.scene.Scene;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class TabManager {

    private final SceneEngine sceneEngine;
    private final SceneConfigurationLoader sceneConfigurationLoader;

    private Map<String, Scene> tabScenes;


    public TabManager(SceneEngine sceneEngine, SceneConfigurationLoader sceneConfigurationLoader) {
        this.sceneEngine = sceneEngine;
        this.sceneConfigurationLoader = sceneConfigurationLoader;
        tabScenes = new HashMap<>();
    }

    public Scene createTabScene(String tabName) {
//        log.info("Creating scene for tab: {}", tabName);
//        return new Scene();

    }

}
