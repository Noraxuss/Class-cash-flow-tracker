package cash_flow.controller;

import cash_flow.context.AppContext;
import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.controller.utilities.TabManager;
import cash_flow.scene.SceneEngine;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Month;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class RequiredPaymentController implements ThemeChangeListener {

    private final StyleManager styleManager;
    private final ControllerUtilities controllerUtilities;
    private final SceneEngine sceneEngine;
    private final TabManager tabManager;
    private final AppContext appContext;


    // FXML-injected nodes
    @FXML private TextField nameTextField;
    @FXML private CheckBox isRecurringPaymentCheckBox;
    @FXML private CheckBox isMultipleInstallmentsCheckBox;

    // Month checkboxes
    @FXML public GridPane monthCheckBox;
    @FXML private CheckBox march;
    @FXML private CheckBox april;
    @FXML private CheckBox may;
    @FXML private CheckBox june;
    @FXML private CheckBox july;
    @FXML private CheckBox august;
    @FXML private CheckBox september;
    @FXML private CheckBox october;
    @FXML private CheckBox november;
    @FXML private CheckBox december;
    @FXML private CheckBox january;
    @FXML private CheckBox february;

    // Store month-checkbox mapping for easy lookups
    private Map<Month, CheckBox> monthCheckBoxMap;

    public RequiredPaymentController(StyleManager styleManager,
                                     ControllerUtilities controllerUtilities,
                                     SceneEngine sceneEngine,
                                     TabManager tabManager,
                                     AppContext appContext) {
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
        this.sceneEngine = sceneEngine;
        this.tabManager = tabManager;
        this.appContext = appContext;
    }

    @FXML
    public void initialize() {
        log.info("Initializing RequiredPaymentController");

        controllerUtilities.initializeSceneStyle(nameTextField, this);


    }


    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(nameTextField.getScene(), this);
    }
}
