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
public class GroupOverviewController implements ThemeChangeListener {

    public final ControllerUtilities controllerUtilities;
    public final SceneEngine sceneEngine;
    public final StyleManager styleManager;

    @FXML
    public Label groupNameLabel;

    @FXML
    public Label groupSizeLabel;

    @FXML
    public Label groupNameValue;

    @FXML
    public Label groupSizeValue;

    public GroupOverviewController(ControllerUtilities controllerUtilities, SceneEngine sceneEngine, StyleManager styleManager) {
        this.controllerUtilities = controllerUtilities;
        this.sceneEngine = sceneEngine;
        this.styleManager = styleManager;
    }

    @FXML
    public void initialize() {
        controllerUtilities.initializeSceneStyle(groupNameLabel, this);
    }

    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(groupNameLabel.getScene(), this);
    }
}
