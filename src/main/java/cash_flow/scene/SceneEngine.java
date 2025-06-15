package cash_flow.scene;

import cash_flow.application.SpringFXMLLoader;
import cash_flow.application.onekeytwovaluemap.OneKeyTwoValueMap;
import cash_flow.controller.BaseLayoutController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
@Slf4j
public class SceneEngine {

    private static final String SCENE_MAP = "/properties/scene_map.properties";

    private final SpringFXMLLoader springFXMLLoader;
    private final OneKeyTwoValueMap<String, String, String> sceneMap;
    private final BaseLayoutController baseLayoutController;

    @Setter
    private Stage mainStage;

    @Autowired
    public SceneEngine(SpringFXMLLoader springFXMLLoader,
                       OneKeyTwoValueMap<String, String, String> sceneMap,
                       BaseLayoutController baseLayoutController) {
        this.springFXMLLoader = springFXMLLoader;
        this.sceneMap = sceneMap;
        this.baseLayoutController = baseLayoutController;
        loadSceneMap();
    }

    private void loadSceneMap() {
        try (InputStream input = getClass().getResourceAsStream(SCENE_MAP)) {
            if (input == null) {
                throw new IOException("Unable to find scenes.properties");
            }

            Properties sceneFiles = new Properties();
            sceneFiles.load(input);

            for (String key : sceneFiles.stringPropertyNames()) {
                String[] values = sceneFiles.getProperty(key).split("=");
                if (values.length == 2) {
                    sceneMap.put(key, values[0], values[1]);

                } else {
                    throw new IllegalArgumentException("Invalid format for key: " + key);
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException("Failed to load scene map", e);
        }
    }

    /**
     * Initializes the primary stage with a base scene and a starter scene.
     */
    public void initializeStage(String starterScene, String loading) {
        log.info("Initializing stage with starter scene: {}", starterScene);

        switchScene(starterScene);
        mainStage.setResizable(true);
        mainStage.show();
        switchScene(loading);

        log.debug("Stage initialized and shown.");
    }

    /**
     * Switches the displayed scene based on its logical placement.
     */
    public void switchScene(String sceneName) {
        log.info("Switching to scene: {}", sceneName);

        String scenePlacement = sceneMap.get(sceneName).getValue2();
        log.debug("Scene placement for {} is '{}'", sceneName, scenePlacement);

        try {
            switch (scenePlacement.toLowerCase()) {
                case "center" -> updateCenterScene(sceneName);
                case "extra" -> createExtraScene(sceneName);
                case "base" -> mainStage.setScene(new Scene(loadScene(sceneName).load()));
//                case "componenet" ->
                default -> throw new IllegalArgumentException("Invalid scene placement: " + scenePlacement);
            }
        } catch (IOException e) {
            log.error("Error switching scene {}: {}", sceneName, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Core scene update logic, handles placement, caching, and controller setup.
     */
    private void updateCenterScene(String sceneName) throws IOException {
        log.info("Updating scene: {}", sceneName);

        Parent scene = loadScene(sceneName).load();
        baseLayoutController.setRightContentPane(scene);
    }

    /**
     * Creates an "extra" scene, which is a separate window (stage) for additional functionality.
     * This method is a placeholder and should be implemented in subclasses.
     */
    private void createExtraScene(String sceneName) throws IOException {
        Parent root = loadScene(sceneName).load();
        Scene scene = new Scene(root);
        Stage extraStage = new Stage();

        log.info("Creating extra stage for scene: {}", sceneName);
        extraStage.setScene(scene);
        extraStage.setResizable(true);
//        this.extraStage.setMaxHeight(ScreenBounds.getScreenBounds().getHeight());
//        this.extraStage.setMaxWidth(ScreenBounds.getScreenBounds().getWidth());
        extraStage.sizeToScene();
        extraStage.centerOnScreen();
        extraStage.initOwner(mainStage);
        extraStage.initModality(Modality.WINDOW_MODAL);
        extraStage.showAndWait();
    }

    public FXMLLoader createSceneComponent(String sceneName) {
        try {
            return loadScene(sceneName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper to load an FXML scene by name from the sceneMap.
     */
    private FXMLLoader loadScene(String name) throws IOException {
        try {
            log.debug("Loading FXML for scene: {}", name);
            String fxmlFile = sceneMap.get(name).getValue1();

            if (fxmlFile == null) {
                throw new IllegalArgumentException("Scene not found in map: " + name);
            }

            return springFXMLLoader.load(fxmlFile);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }


}
