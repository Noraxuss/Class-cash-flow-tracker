package cash_flow.controller.utilities;

import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
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

    public void initializeSceneStyle(Node node, ThemeChangeListener listener) {
        styleManager.addListener(listener);

        // We attach a listener to the sceneProperty of the node (the root node of this cell).
        // This listener triggers when the cell is actually attached to a Scene, which is when we can safely
        // access the Stage. This avoids premature access (and null pointers) that would happen in initialize().
        node.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                // Once the node has a Scene, we can now retrieve the Window (Stage) it's part of
                Stage stage = (Stage) newScene.getWindow();
                // Clear existing stylesheets to avoid duplicates
                node.getScene().getStylesheets().clear();

                styleManager.toggleSceneStyle(node.getScene(), listener);

                if (stage != null) {
                    // Register a cleanup hook: if the window is closed, unregister this controller
                    // from the StyleManager to avoid memory leaks or unnecessary updates
                    stage.setOnCloseRequest(event -> {
                        log.info("Stage closed, removing theme listener from StyleManager");
                        styleManager.removeListener(listener);
                    });
                } else {
                    // This warning shouldn't normally appear — but it's helpful in debugging if something odd happens
                    log.warn("Stage was null when scene became available — close listener not set.");
                }
            }
        });

    }

}
