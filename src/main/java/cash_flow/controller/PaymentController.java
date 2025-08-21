package cash_flow.controller;

import cash_flow.context.AppContext;
import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.controller.utilities.TabManager;
import cash_flow.scene.SceneEngine;
import cash_flow.style_manager.StyleManager;
import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentController {

    private final StyleManager styleManager;
    private final ControllerUtilities controllerUtilities;
    private final SceneEngine sceneEngine;
    private final TabManager tabManager;
    private final AppContext appContext;

    public PaymentController(StyleManager styleManager, ControllerUtilities controllerUtilities, SceneEngine sceneEngine, TabManager tabManager, AppContext appContext) {
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
        this.sceneEngine = sceneEngine;
        this.tabManager = tabManager;
        this.appContext = appContext;
    }

    @FXML
    public void initialize() {
        // TODO: Initialize your scene here
    }
}
