package cash_flow.controller;

import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import org.springframework.stereotype.Component;

@Component
public class GroupDataController implements ThemeChangeListener {

    private final StyleManager styleManager;
    private final ControllerUtilities controllerUtilities;

    @FXML
    private TabPane groupDataTabPane;

    public GroupDataController(StyleManager styleManager, ControllerUtilities controllerUtilities) {
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
    }

    @FXML
    public void initialize() {
        controllerUtilities.initializeSceneStyle(groupDataTabPane, this);
    }

    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(groupDataTabPane.getScene(), this);
    }
}
