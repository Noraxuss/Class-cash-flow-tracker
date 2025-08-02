package cash_flow.controller;

import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.controller.utilities.TabManager;
import cash_flow.scene.SceneEngine;
import cash_flow.scene.SceneType;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;
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

    @FXML
    public TabPane membersTabPane;
    @FXML
    public Tab addMemberTab;
    @FXML
    public TabPane groupTabPane;
    @FXML
    public Label addMember;
    @FXML
    public SplitPane splitPane;
    @FXML
    private VBox leftPane;
    @FXML
    private VBox rightPane;

    // Constants for divider positions
    private static final double LEFT_ACTIVE_POSITION = 0.1;
    private static final double RIGHT_ACTIVE_POSITION = 0.9;

    public SplitCenterController(StyleManager styleManager,
                                 ControllerUtilities controllerUtilities,
                                 SceneEngine sceneEngine,
                                 TabManager tabManager) {
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
        this.sceneEngine = sceneEngine;
        this.tabManager = tabManager;
    }

    @FXML
    public void initialize() {
        log.info("SplitCenterController initialize");
        controllerUtilities.initializeSceneStyle(addMember, this);

        // Initialize tabs
        groupTabPane.getTabs().add(tabManager.createTab(SceneType.GROUP_OVERVIEW));
        membersTabPane.getTabs().add(tabManager.createTab(SceneType.GROUP_MEMBER_DATA));

        // --- Mouse click listener (fires for any mouse click on the TabPane, anywhere) ---
        groupTabPane.setOnMouseClicked(event -> {
            Tab selectedTab = groupTabPane.getSelectionModel().getSelectedItem();
            if (selectedTab != null) {
                log.info("Left tab selected -> moving divider right");
                setActiveTabPane(groupTabPane, membersTabPane);
                animateDivider(RIGHT_ACTIVE_POSITION); // Show left side larger
            }
        });

        membersTabPane.setOnMouseClicked(event -> {
            Tab selectedTab = membersTabPane.getSelectionModel().getSelectedItem();
            if (selectedTab != null && selectedTab != addMemberTab) {
                log.info("Right tab selected -> moving divider left");
                setActiveTabPane(membersTabPane, groupTabPane);
                animateDivider(LEFT_ACTIVE_POSITION); // Show right side larger
            }
            if (selectedTab == addMemberTab) {
                createAddMemberTab();
            }
        });

        membersTabPane.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            for (Tab tab : membersTabPane.getTabs()) {
                Node graphic = tab.getGraphic();
                if (graphic instanceof Region region) {
                    region.setPrefWidth(newWidth.doubleValue() * 0.15); // 15% of tabPane width
                }
            }
        });

        for (Tab tab : membersTabPane.getTabs()) {
            setupHoverEffectForTab(tab);
        }
        for (Tab tab : groupTabPane.getTabs()) {
            setupHoverEffectForTab(tab);
        }
        groupTabPane.getSelectionModel().select(0);
        setActiveTabPane(groupTabPane, membersTabPane);
        animateDivider(RIGHT_ACTIVE_POSITION); // Show left side larger
    }

    /**
     * Create the "+" tab logic (never focusable, adds new member tabs).
     */
    private void createAddMemberTab() {
        log.info("create AddMemberTab");

        // Create a new member tab before the "+"
        Tab newMemberTab = tabManager.createTab(SceneType.GROUP_MEMBER_DATA);
        Label newMemberLabel = new Label("New Member");
        newMemberTab.setGraphic(newMemberLabel);
        setupHoverEffectForTab(newMemberTab); // <--- Add hover behavior
        membersTabPane.getTabs().add(membersTabPane.getTabs().size() - 1, newMemberTab);

        // Select the new tab
        membersTabPane.getSelectionModel().select(newMemberTab);
        setActiveTabPane(groupTabPane, membersTabPane);
        animateDivider(RIGHT_ACTIVE_POSITION); // Show left side larger
    }

    /**
     * Animate the SplitPane divider.
     */
    private void animateDivider(double targetPosition) {
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(
                splitPane.getDividers().getFirst().positionProperty(),
                targetPosition,
                Interpolator.EASE_BOTH
        );
        KeyFrame kf = new KeyFrame(Duration.millis(300), kv);
        timeline.getKeyFrames().setAll(kf);
        timeline.play();
    }

    private void setupHoverEffectForTab(Tab tab) {
//        if (tab.getGraphic() != null) {
//            Node headerNode = tab.getGraphic();
//
//            // Default width
//            headerNode.prefWidth(60);
//
//            // Hover animation
//            headerNode.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
//                if (isNowHovered) {
//                    animateWidth(headerNode, 120);
//                } else {
//                    animateWidth(headerNode, 60);
//                }
//            });
//        }
    }

    private void animateWidth(Node node, double targetWidth) {
        if (node instanceof Region region) {
            Timeline timeline = new Timeline();
            KeyValue kv = new KeyValue(
                    region.prefWidthProperty(),
                    targetWidth,
                    Interpolator.EASE_BOTH);
            KeyFrame kf = new KeyFrame(Duration.millis(200), kv);
            timeline.getKeyFrames().add(kf);
            timeline.play();
        }
    }

    /**
     * Update tab width classes depending on active side.
     */
    private void setActiveTabPane(TabPane active, TabPane inactive) {
        active.getStyleClass().add("active");
        inactive.getStyleClass().remove("active");
    }

    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(leftPane.getScene(), this);
    }
}
