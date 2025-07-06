package cash_flow.controller;

import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BaseLayoutController implements ThemeChangeListener {

    @FXML
    public MenuBar mainMenuBar;

    @FXML
    public Pane centerPane;

//    @FXML
//    public VBox rightContentPane;
//
//    @FXML
//    public VBox leftContentPane;

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

    public void setCenterContentPanes(Node node) {
        clearCenterContentPane();
        if (centerPane.getChildren().isEmpty()) {
            centerPane.getChildren().add(node);
            log.info("Node added to center content pane");
        } else {
            centerPane.getChildren().set(0, node);
            log.info("Node replaced in center content pane");
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
