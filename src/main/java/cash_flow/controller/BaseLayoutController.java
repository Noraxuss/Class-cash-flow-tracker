package cash_flow.controller;

import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BaseLayoutController implements ThemeChangeListener {

    @FXML
    public MenuBar mainMenuBar;

    @FXML
    public SplitPane centerSplitPane;

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

    public void setCenterContentPane(Node ...contentPane) {
        if (contentPane == null || contentPane.length == 0) {
            log.warn("No content provided to setRightContentPane");
            return;
        }
        centerSplitPane.getItems().addAll(contentPane);
        log.info("Right content pane set with {} items", contentPane.length);
    }

    public void clearCenterContentPane() {
        centerSplitPane.getItems().clear();
        log.info("Center content pane cleared");
    }


    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(centerSplitPane.getScene(), this);
    }

}
