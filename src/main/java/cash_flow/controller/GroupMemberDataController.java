package cash_flow.controller;

import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.scene.SceneEngine;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GroupMemberDataController implements ThemeChangeListener {

    public final ControllerUtilities controllerUtilities;
    public final SceneEngine sceneEngine;
    public final StyleManager styleManager;

    @FXML
    public Label nameLabel;

    @FXML
    public Label nameValue;

    public GroupMemberDataController(ControllerUtilities controllerUtilities, SceneEngine sceneEngine, StyleManager styleManager) {
        this.controllerUtilities = controllerUtilities;
        this.sceneEngine = sceneEngine;
        this.styleManager = styleManager;
    }

    @FXML
    public void initialize() {
        log.info("Initializing GroupMemberDataController");
        controllerUtilities.initializeSceneStyle(nameLabel, this);

    }

    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(nameLabel.getScene(), this);
    }
}
