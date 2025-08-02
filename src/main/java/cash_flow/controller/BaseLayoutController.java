package cash_flow.controller;

import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.scene.SceneEngine;
import cash_flow.scene.SceneType;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BaseLayoutController implements ThemeChangeListener {

    private final StyleManager styleManager;
    private final ControllerUtilities controllerUtilities;
    private final SceneEngine sceneEngine;

    @FXML
    public MenuBar mainMenuBar;

    @FXML
    public AnchorPane centerPane;

    @FXML
    public MenuItem addMembers;


    public BaseLayoutController(StyleManager styleManager, ControllerUtilities controllerUtilities, SceneEngine sceneEngine) {
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
        this.sceneEngine = sceneEngine;
    }

    @FXML
    public void initialize() {
        log.info("BaseLayoutController initialized");
        controllerUtilities.initializeSceneStyle(mainMenuBar, this);
        Platform.runLater(() -> {
            log.info("Switching to initial scene: {}", SceneType.GROUP_CHOOSING);
            sceneEngine.loadingNextScene(SceneType.GROUP_CHOOSING);
        });
        addMembers.setOnAction(event -> {
            log.info("Add Members menu item clicked");
            sceneEngine.loadingNextScene(SceneType.ADD_GROUP_MEMBERS);
        });
    }

    public void setCenterContentPanes(Node node) {
        clearCenterContentPane();

        if (centerPane.getChildren().isEmpty()) {
            centerPane.getChildren().add(node);

            // Anchor all edges so it fills the space
            AnchorPane.setTopAnchor(node, 0.0);
            AnchorPane.setRightAnchor(node, 0.0);
            AnchorPane.setBottomAnchor(node, 0.0);
            AnchorPane.setLeftAnchor(node, 0.0);

            log.info("Node added and anchored to centerPane");
        }
    }

    public void clearCenterContentPane() {
        centerPane.getChildren().clear();
        log.info("Center content pane cleared");
    }


    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(centerPane.getScene(), this);
    }

}
