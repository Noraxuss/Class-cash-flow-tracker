package cash_flow.controller;

import cash_flow.context.AppContext;
import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.controller.utilities.TabManager;
import cash_flow.scene.SceneEngine;
import cash_flow.scene.SceneType;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
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
    @FXML private TabPane groupTabPane, membersTabPane;
    @FXML private Tab addMemberTab;
    @FXML private Label addMember;

    private static final double LEFT_ACTIVE_POSITION = 0.1;
    private static final double RIGHT_ACTIVE_POSITION = 0.9;

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

        controllerUtilities.initializeSceneStyle(addMember, this);

        groupTabPane.getTabs().add(tabManager.createTab(SceneType.GROUP_OVERVIEW));
        membersTabPane.getTabs().add(tabManager.createTab(SceneType.GROUP_MEMBER_DATA));

        groupTabPane.setOnMouseClicked(e -> {
            if (groupTabPane.getSelectionModel().getSelectedItem() != null) {
                setActiveTabPane(groupTabPane, membersTabPane);
                animateDividerPosition(RIGHT_ACTIVE_POSITION);
            }
        });

        membersTabPane.setOnMouseClicked(e -> {
            Tab selected = membersTabPane.getSelectionModel().getSelectedItem();
            if (selected == addMemberTab) {
                createNewMemberTab();
            } else if (selected != null) {
                setActiveTabPane(membersTabPane, groupTabPane);
                animateDividerPosition(LEFT_ACTIVE_POSITION);
            }
        });

        groupTabPane.getSelectionModel().selectFirst();
        setActiveTabPane(groupTabPane, membersTabPane);
        animateDividerPosition(RIGHT_ACTIVE_POSITION);
        Platform.runLater(() -> {widenTabsHorizontally(groupTabPane, 120);
            widenTabsHorizontally(membersTabPane, 120);});

        // Add listener to dynamically resize on tabs changes
        groupTabPane.getTabs().addListener((ListChangeListener<Tab>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    widenTabsHorizontally(groupTabPane, 120);
                }
            }
        });

        membersTabPane.getTabs().addListener((ListChangeListener<Tab>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    widenTabsHorizontally(membersTabPane, 120);
                }
            }
        });


    }

    private void createNewMemberTab() {
        Tab newTab = tabManager.createTab(SceneType.GROUP_MEMBER_DATA);
        newTab.setGraphic(new Label("New Member"));
        membersTabPane.getTabs().add(membersTabPane.getTabs().size() - 1, newTab);
        membersTabPane.getSelectionModel().select(newTab);
        setActiveTabPane(groupTabPane, membersTabPane);
        animateDividerPosition(RIGHT_ACTIVE_POSITION);
    }

    private void animateDividerPosition(double target) {
        DoubleProperty pos = splitPane.getDividers().getFirst().positionProperty();
        Timeline tl = new Timeline(
                new KeyFrame(
                        Duration.millis(300),
                        new KeyValue(
                                pos,
                                target,
                                Interpolator.EASE_BOTH)));
        tl.play();
    }

    private void widenTabsHorizontally(TabPane tabPane, double width) {
        // Force CSS and layout so headers exist
        tabPane.applyCss();
        tabPane.layout();

        var tabHeaderArea = tabPane.lookup(".tab-header-area");
        if (!(tabHeaderArea instanceof Region headerRegion)) {
            log.warn("No .tab-header-area found or not a Region for {}", tabPane);
            return;
        }

        // Set width on tab header area itself
        headerRegion.setMaxHeight(width);
        headerRegion.setPrefHeight(width);
        headerRegion.setMinHeight(width);

        var tabHeaders = headerRegion.lookupAll(".tab");
        for (var node : tabHeaders) {
            if (node instanceof Region tabHeader) {
                tabHeader.setMinHeight(width);
                tabHeader.setPrefHeight(width);
                tabHeader.setMaxHeight(width);
                tabHeader.setMinWidth(60);
                tabHeader.setPrefWidth(60);
                tabHeader.setMaxWidth(60);

                // Optionally set padding or margin on tab header if needed
                tabHeader.setPadding(new Insets(0, 10, 0, 10)); // horizontal padding
            }
        }

        // Force layout pass to apply changes
        tabPane.requestLayout();
        tabPane.layout();
    }


    private void setActiveTabPane(TabPane active, TabPane inactive) {
        active.getStyleClass().add("active");
        inactive.getStyleClass().remove("active");
    }

    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(groupTabPane.getScene(), this);
    }
}
