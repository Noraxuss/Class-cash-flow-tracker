package cash_flow.scene;

import cash_flow.application.SpringFXMLLoader;
import cash_flow.context.AppContext;
import cash_flow.controller.BaseLayoutController;
import cash_flow.controller.SplitCenterController;
import cash_flow.controller.utilities.DeferredSceneInit;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    private final AppContext appContext;
    private final SplitCenterController splitCenterController;

    @Setter
    private Stage mainStage;

    private boolean isFirstSceneLoad = true;

    /**
     * Constructor for SceneEngine, initializes the dependencies.
     *
     * @param springFXMLLoader         the loader for FXML files
     * @param baseLayoutController     the controller for the base layout
     * @param sceneConfigurationLoader the loader for scene configurations
     * @param appContext               the application context
     */
    @Autowired
    public SceneEngine(SpringFXMLLoader springFXMLLoader,
                       @Lazy BaseLayoutController baseLayoutController,
                       SceneConfigurationLoader sceneConfigurationLoader,
                       AppContext appContext,
                       @Lazy SplitCenterController splitCenterController) {
        this.springFXMLLoader = springFXMLLoader;
        this.baseLayoutController = baseLayoutController;
        this.sceneConfigurationLoader = sceneConfigurationLoader;
        this.appContext = appContext;
        this.splitCenterController = splitCenterController;
    }

    /**
     * Initializes the main stage with the specified starter scene and loading scene.
     * This method sets up the initial scene and displays the main stage.
     *
     * @param starterScene the initial scene to display
     */
    public void initializeStage(SceneType starterScene) {
        log.info("Initializing loading with scene: {}", "LOADING");
        switchScene(SceneType.LOADING);

        log.info("Initializing main stage with scene: {}", starterScene);
        switchScene(starterScene);
        mainStage.show();
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
        // hide loadingStage since loading is finished
        log.info("Hiding loading scene after switching to {}", sceneType);
        hideLoadingScene();
    }

    /**
     * Loads the next scene and displays a loading scene while the new scene is being prepared.
     * This method is useful for transitions between scenes, especially during application startup.
     *
     * @param nextScene the type of the next scene to load
     */
    public void loadingNextScene(SceneType nextScene) {
        log.info("Loading next scene: {}", nextScene);

        showLoadingScene();

        // If this is the first scene load, we add a delay to allow the loading scene to be visible
        // before switching to the next scene. This is useful for transitions.
        if (isFirstSceneLoad) {
            this.isFirstSceneLoad = false;
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.3));
            pauseTransition.setOnFinished(event -> {
                switchScene(nextScene);  // This automatically hides the loader
            });
            pauseTransition.play();
        } else {
            switchScene(nextScene);
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

    /**
     * Updates the center scene of the application.
     * This method loads the specified scene and sets it as the center content pane.
     *
     * @param configuration the scene configuration containing FXML and CSS paths
     * @throws IOException if the FXML file cannot be loaded
     */
    private void updateCenterScene(SceneConfiguration configuration) throws IOException {
        baseLayoutController.clearCenterContentPane();

        log.info("Updating scene: {}", configuration);

        FXMLLoader loader = loadScene(configuration);
        Parent scene = loader.load();

        // Auto-size scene to fit the container
        if (scene instanceof SplitPane splitPane) {
            // Ensure it grows and fills the space in its parent SplitPane
            SplitPane.setResizableWithParent(splitPane, true);
        }

        baseLayoutController.setCenterContentPanes(scene);

        // Handle Deferred init
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
        log.info("Creating extra layout for scene: {}", configuration);
        FXMLLoader fxmlLoader = loadScene(configuration);

        // Load the FXML file and create the scene
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage extraStage = new Stage();

        log.info("Creating extra stage for scene: {}", configuration);
        extraStage.setScene(scene);
        extraStage.setResizable(configuration.isResizable());
        extraStage.initOwner(mainStage);
        extraStage.centerOnScreen();
        extraStage.initModality(Modality.APPLICATION_MODAL);

        if (sceneType != SceneType.LOADING) {
            extraStage.sizeToScene();
            // This allows interaction with the main stage while the extra stage is open
            log.info("Showing extra stage for scene: {}", sceneType);
            extraStage.show();
        }
        if (sceneType == SceneType.LOADING) {
            extraStage.initStyle(StageStyle.UNDECORATED); // 🔥 This removes the title bar
            extraStage.setAlwaysOnTop(true);
            appContext.getLoadingSceneContext().setLoadingStage(extraStage);
            log.info("Loading stage set in AppContext: {}", appContext.getLoadingSceneContext().getLoadingStage());
            extraStage.show();
        }
    }

    /**
     * Shows the loading scene, typically used during application startup or when switching scenes.
     * This method displays a loading indicator to inform the user that the application is processing.
     */
    public void showLoadingScene() {
        log.info("Showing loading scene");
        Stage loadingStage = appContext.getLoadingSceneContext().getLoadingStage();

        loadingStage.show();
    }

    /**
     * Hides the loading scene if it is currently displayed.
     * This method is called when the application is ready to switch to the next scene.
     */
    public void hideLoadingScene() {
        log.info("Hiding loading scene");
        Stage loadingStage = appContext.getLoadingSceneContext().getLoadingStage();
        if (loadingStage != null) {
            loadingStage.hide();
        } else {
            log.warn("Loading stage is null, cannot hide loading scene.");
        }
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

    /**
     * Gets the scene for the specified scene type.
     * This method loads the FXML and returns a new Scene instance.
     *
     * @param sceneType the type of scene to get
     * @return a new Scene instance for the specified scene type
     */
    public Scene getScene(SceneType sceneType) {
        try {
            FXMLLoader loader = createSceneComponent(sceneType);
            Parent root = loader.load();
            return new Scene(root);
        } catch (IOException e) {
            log.error("Error loading scene {}: {}", sceneType, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
