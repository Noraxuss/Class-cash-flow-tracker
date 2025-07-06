package cash_flow.scene;

import cash_flow.application.SpringFXMLLoader;
import cash_flow.context.AppContext;
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

    /**
     * The SceneEngine is responsible for managing the application's scenes.
     * It handles scene switching, loading FXML files, and applying styles.
     * This class is a Spring component and uses dependency injection for its dependencies.
     */

    private final SpringFXMLLoader springFXMLLoader;
    private final BaseLayoutController baseLayoutController;
    private final SceneConfigurationLoader sceneConfigurationLoader;
    private final StyleManager styleManager;
    private final AppContext appContext;

    @Setter
    private Stage mainStage;

    /**
     * Constructor for SceneEngine, initializes the dependencies.
     *
     * @param springFXMLLoader        the loader for FXML files
     * @param baseLayoutController    the controller for the base layout
     * @param sceneConfigurationLoader the loader for scene configurations
     * @param styleManager            the manager for styles
     * @param appContext              the application context
     */
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
     * Initializes the main stage with the specified starter scene and loading scene.
     * This method sets up the initial scene and displays the main stage.
     *
     * @param starterScene the initial scene to display
     * @param loading      the loading scene to show while initializing
     */
    public void initializeStage(SceneType starterScene, SceneType loading) {
        log.info("Initializing stage with starter scene: {}", starterScene);

        switchScene(starterScene);
        mainStage.show();
        switchScene(loading);

        log.debug("Stage initialized and shown.");
    }

    /**
     * Switches the current scene to the specified scene type.
     * This method handles different placements of scenes (center, extra, base) based on the configuration.
     *
     * @param sceneType the type of scene to switch to
     */
    public void switchScene(SceneType sceneType) {
        SceneConfiguration sceneConfiguration = sceneConfigurationLoader.load(sceneType);
        log.info("Switching to scene: {}", sceneType);

        String scenePlacement = sceneConfiguration.getPlacement();
        log.debug("Scene placement for {} is '{}'", sceneType, scenePlacement);

        try {
            switch (scenePlacement.toLowerCase()) {
                case "center" -> updateCenterScene(sceneConfiguration);
                case "extra" -> createExtraScene(sceneConfiguration, sceneType);
                case "base" -> createBaseLayout(sceneConfiguration);
                default -> throw new IllegalArgumentException("Invalid scene placement: " + scenePlacement);
            }
        } catch (IOException e) {
            log.error("Error switching scene {}: {}", sceneType, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates the base layout for the application.
     * This method sets up the main stage with the base layout scene.
     *
     * @param sceneConfiguration the configuration for the base layout scene
     * @throws IOException if the FXML file cannot be loaded
     */
    private void createBaseLayout(SceneConfiguration sceneConfiguration) throws IOException {
        log.info("Creating base layout for scene: {}", sceneConfiguration);
        FXMLLoader loader = loadScene(sceneConfiguration);
        Parent load = loader.load();
        Scene scene = new Scene(load);
        mainStage.setScene(scene);
        mainStage.setResizable(sceneConfiguration.isResizable());
    }

    private void updateCenterScene(SceneConfiguration configuration) throws IOException {
        baseLayoutController.clearCenterContentPane();

        log.info("Updating scene: {}", configuration);

        FXMLLoader loader = loadScene(configuration);
        Parent scene = loader.load();
        baseLayoutController.setCenterContentPanes(scene);

        Platform.runLater(() -> {
            Object controller = loader.getController();
            if (controller instanceof DeferredSceneInit deferred) {
                deferred.onSceneLoad();
            }
        });
    }

    /**
     * Creates an extra scene displayed as a modal dialog.
     * This method is used for scenes that require user interaction without leaving the main scene.
     *
     * @param configuration the scene configuration containing FXML and CSS paths
     * @param sceneType     the type of scene to create
     * @throws IOException if the FXML file cannot be loaded
     */
    private void createExtraScene(SceneConfiguration configuration, SceneType sceneType) throws IOException {
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

        appContext.getStageContext().addStage(sceneType, extraStage);// Store the extra stage in the context
    }

    /**
     * Creates a scene component for the given scene type.
     * This method loads the FXML and applies any necessary styles.
     *
     * @param sceneType the type of scene to create
     * @return FXMLLoader instance for the loaded scene
     */
    public FXMLLoader createSceneComponent(SceneType sceneType) {
        SceneConfiguration sceneConfiguration = sceneConfigurationLoader.load(sceneType);
        try {
            return loadScene(sceneConfiguration);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the FXML file for the given scene configuration.
     * This method is responsible for loading the FXML and applying any necessary styles.
     *
     * @param configuration the scene configuration containing FXML and CSS paths
     * @return FXMLLoader instance for the loaded scene
     * @throws IOException if the FXML file cannot be loaded
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
