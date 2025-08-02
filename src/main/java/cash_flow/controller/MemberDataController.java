package cash_flow.controller;

import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberDataController implements ThemeChangeListener {

    private final StyleManager styleManager;
    private final ControllerUtilities controllerUtilities;

    @FXML
    private TabPane memberDataTabPane;

    @Autowired
    public MemberDataController(StyleManager styleManager, ControllerUtilities controllerUtilities) {
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
    }

    @FXML
    public void initialize() {
        controllerUtilities.initializeSceneStyle(memberDataTabPane, this);
    }

    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(memberDataTabPane.getScene(), this);
    }
}
