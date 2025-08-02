package cash_flow.controller.utilities;

import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ControllerUtilities {

    private final StyleManager styleManager;

    public ControllerUtilities(StyleManager styleManager) {
        this.styleManager = styleManager;
    }

    /**
     * Initializes the scene style for a given Node and registers a ThemeChangeListener.
     * This method ensures that the stylesheet is applied when the scene is attached to the Node.
     *
     * @param node     The Node to apply the style to.
     * @param listener The ThemeChangeListener to register for style changes.
     */
    public void initializeSceneStyle(Node node, ThemeChangeListener listener) {
        styleManager.addListener(listener);

        node.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                // Apply stylesheet immediately when the scene is attached
                newScene.getStylesheets().clear();
                styleManager.toggleSceneStyle(newScene, listener);

                // Delay accessing the Stage to give JavaFX time to attach the Window
                Platform.runLater(() -> {
                    if (newScene.getWindow() instanceof Stage stage) {
                        stage.setOnCloseRequest(event -> {
                            log.info("Stage closed, removing theme listener from StyleManager");
                            styleManager.removeListener(listener);
                        });
                    } else {
                        log.warn("Stage was still null after Platform.runLater — close listener not set.");
                    }
                });
            }
        });
    }

    public void closeStage(Node node) {
        if (node != null && node.getScene() != null && node.getScene().getWindow() instanceof Stage stage) {
            stage.close();
        } else {
            log.warn("Node's scene or window is null, cannot close stage.");
        }
    }


}
