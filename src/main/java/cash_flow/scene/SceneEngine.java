package cash_flow.scene;

import cash_flow.application.SpringFXMLLoader;
import cash_flow.context.AppContext;
import cash_flow.context.StageContext;
import cash_flow.controller.BaseLayoutController;
import cash_flow.controller.DeferredSceneInit;
import cash_flow.style_manager.StyleManager;
import javafx.application.Platform;
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

@Component
@Slf4j
public class SceneEngine {

    private final SpringFXMLLoader springFXMLLoader;
    private final BaseLayoutController baseLayoutController;
    private final SceneConfigurationLoader sceneConfigurationLoader;
    private final StyleManager styleManager;
    private final AppContext appContext;

    @Setter
    private Stage mainStage;

    @Autowired
    public SceneEngine(SpringFXMLLoader springFXMLLoader,
                       BaseLayoutController baseLayoutController,
                       SceneConfigurationLoader sceneConfigurationLoader,
                       StyleManager styleManager, AppContext appContext) {
        this.springFXMLLoader = springFXMLLoader;
        this.baseLayoutController = baseLayoutController;
        this.sceneConfigurationLoader = sceneConfigurationLoader;
        this.styleManager = styleManager;
        this.appContext = appContext;
    }

    /**
     * Initializes the primary stage with a base scene and a starter scene.
     */
    public void initializeStage(SceneType starterScene, SceneType loading) {
        log.info("Initializing stage with starter scene: {}", starterScene);

        switchScene(starterScene);
        mainStage.show();
        switchScene(loading);



        log.debug("Stage initialized and shown.");
    }

    /**
     * Switches the displayed scene based on its logical placement.
     */
    public void switchScene(SceneType sceneType) {
        SceneConfiguration sceneConfiguration = sceneConfigurationLoader.load(sceneType);
        log.info("Switching to scene: {}", sceneType);

        String scenePlacement = sceneConfiguration.getPlacement();
        log.debug("Scene placement for {} is '{}'", sceneType, scenePlacement);

        try {
            switch (scenePlacement.toLowerCase()) {
                case "center" -> updateCenterScene(sceneConfiguration);
                case "extra" -> createExtraScene(sceneConfiguration);
                case "base" -> createBaseLayout(sceneConfiguration);
//                case "componenet" ->
                default -> throw new IllegalArgumentException("Invalid scene placement: " + scenePlacement);
            }
        } catch (IOException e) {
            log.error("Error switching scene {}: {}", sceneType, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void createBaseLayout(SceneConfiguration sceneConfiguration) throws IOException {
        FXMLLoader loader = loadScene(sceneConfiguration);
        Parent load = loader.load();
        Scene scene = new Scene(load);
//        scene.getStylesheets().add(sceneConfiguration.getCssLight());
        mainStage.setScene(scene);
        mainStage.setResizable(sceneConfiguration.isResizable());
    }

    /**
     * Core scene update logic, handles placement, caching, and controller setup.
     */
    private void updateCenterScene(SceneConfiguration configuration) throws IOException {
        log.info("Updating scene: {}", configuration);

        FXMLLoader loader = loadScene(configuration);
        Parent scene = loader.load();
//        scene.getStylesheets().add(configuration.getCssLight());
        baseLayoutController.setRightContentPane(scene);

        Platform.runLater(() -> {
            Object controller = loader.getController();
            if (controller instanceof DeferredSceneInit deferred) {
                deferred.onSceneLoad();
            }
        });
    }

    /**
     * Creates an "extra" scene, which is a separate window (stage) for additional functionality.
     * This method is a placeholder and should be implemented in subclasses.
     */
    private void createExtraScene(SceneConfiguration configuration) throws IOException {
        FXMLLoader fxmlLoader = loadScene(configuration);

        Parent root = fxmlLoader.load();
//        root.getStylesheets().add(configuration.getCssLight());
        Scene scene = new Scene(root);
        Stage extraStage = new Stage();

        log.info("Creating extra stage for scene: {}", configuration);
        extraStage.setScene(scene);
        extraStage.setResizable(configuration.isResizable());
//        this.extraStage.setMaxHeight(ScreenBounds.getScreenBounds().getHeight());
//        this.extraStage.setMaxWidth(ScreenBounds.getScreenBounds().getWidth());
        extraStage.sizeToScene();
        extraStage.centerOnScreen();
        extraStage.initOwner(mainStage);
        extraStage.initModality(Modality.WINDOW_MODAL);
        extraStage.showAndWait();

        appContext.getStageContext().setScenes();
    }

    public FXMLLoader createSceneComponent(SceneType sceneType) {
        SceneConfiguration sceneConfiguration = sceneConfigurationLoader.load(sceneType);
        try {
            return loadScene(sceneConfiguration);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper to load an FXML scene by name from the sceneMap.
     */
    private FXMLLoader loadScene(SceneConfiguration configuration) throws IOException {
        try {
            log.debug("Loading FXML for scene: {}", configuration);

            if (configuration.getFxml() == null) {
                throw new IllegalArgumentException("Scene not found in map: " + configuration.getId());
            }

            return springFXMLLoader.load(configuration);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeScene(SceneType scene) {

    }

}
