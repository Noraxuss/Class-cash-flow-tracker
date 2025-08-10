package cash_flow.controller;

import cash_flow.context.AppContext;
import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.controller.utilities.TabManager;
import cash_flow.scene.SceneEngine;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class SplitCenterController implements ThemeChangeListener {

    private final StyleManager styleManager;
    private final ControllerUtilities controllerUtilities;
    private final SceneEngine sceneEngine;
    private final TabManager tabManager;
    private final AppContext appContext;

    @FXML private SplitPane splitPane;

    @FXML public VBox leftGroupTabs;
    @FXML public VBox leftGroupContent;
    @FXML public VBox rightMemberContent;
    @FXML public VBox rightMemberTabs;

    // Dividers
    private SplitPane.Divider leftDivider;
    private SplitPane.Divider centerDivider;
    private SplitPane.Divider rightDivider;

    public SplitCenterController(StyleManager styleManager,
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
        log.info("SplitCenterController initialized");
        controllerUtilities.initializeSceneStyle(splitPane, this);

        setDividers();
        leftFocus();
    }

    private void setDividers() {
        leftDivider = splitPane.getDividers().getFirst();
        centerDivider = splitPane.getDividers().get(1);
        rightDivider = splitPane.getDividers().getLast();
    }

    private void leftFocus() {
        //animate the rightDivider to reflect the buttons size
        animateDividerPosition(leftDivider, 0.08);
        animateDividerPosition(centerDivider, 0.92);
        animateDividerPosition(rightDivider, 0.92);
    }

    private void rightFocus() {
        //animate the rightDivider to reflect the buttons size
        animateDividerPosition(leftDivider, 0.92);
        animateDividerPosition(centerDivider, 0.08);
        animateDividerPosition(rightDivider, 0.08);
    }

    private void animateDividerPosition(SplitPane.Divider divider,double target) {
        DoubleProperty position = divider.positionProperty();
        Timeline tl = new Timeline(
                new KeyFrame(
                        Duration.millis(300),
                        new KeyValue(
                                position,
                                target,
                                Interpolator.EASE_BOTH)));
        tl.play();
    }

    private void setActiveTabPane(TabPane active, TabPane inactive) {
        active.getStyleClass().add("active");
        inactive.getStyleClass().remove("active");
    }

    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(splitPane.getScene(), this);
    }
}
