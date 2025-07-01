package cash_flow.controller;

import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BaseLayoutController implements ThemeChangeListener {

    @FXML
    public MenuBar mainMenuBar;

    @FXML
    public MenuBar leftSubMenuBar;

    @FXML
    public TreeView collectionGroupDataTree;

    @FXML
    public VBox rightContentPane;

    private final StyleManager styleManager;
    private final ControllerUtilities controllerUtilities;

    public BaseLayoutController(StyleManager styleManager, ControllerUtilities controllerUtilities) {
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
    }

    @FXML
    public void initialize() {
        log.info("BaseLayoutController initialized");
        controllerUtilities.initializeSceneStyle(mainMenuBar, this);
    }

    public void setRightContentPane(Node rightContentPane) {
        this.rightContentPane.getChildren().setAll(rightContentPane);
    }


    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(rightContentPane.getScene(), this);
    }

}
