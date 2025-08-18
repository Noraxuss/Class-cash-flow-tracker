package cash_flow.controller;

import cash_flow.context.AppContext;
import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.controller.utilities.GroupOverViewEnum;
import cash_flow.controller.utilities.TabManager;
import cash_flow.dto.outgoing.MemberOverviewDetails;
import cash_flow.scene.SceneConfigurationLoader;
import cash_flow.scene.SceneEngine;
import cash_flow.scene.SceneType;
import cash_flow.service.MemberService;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


@Component
@Slf4j
public class SplitCenterController implements ThemeChangeListener {

    private final StyleManager styleManager;
    private final ControllerUtilities controllerUtilities;
    private final SceneEngine sceneEngine;
    private final TabManager tabManager;
    private final AppContext appContext;
    private final MemberService memberService;
    private final SceneConfigurationLoader sceneConfigurationLoader;

    @FXML
    private SplitPane splitPane;

    @FXML
    public VBox leftGroupTabs;
    @FXML
    public StackPane leftGroupContent;
    @FXML
    public StackPane rightMemberContent;
    @FXML
    public VBox rightMemberTabs;

    @FXML
    public ResourceBundle resources;

    // Dividers
    private SplitPane.Divider leftDivider;
    private SplitPane.Divider centerDivider;
    private SplitPane.Divider rightDivider;

    private final Map<String, Parent> leftContentMap = new HashMap<>();
    private final Map<String, Parent> rightContentMap = new HashMap<>();

    private final ObservableList<Button> rightMemberButtons = FXCollections.observableArrayList();
    private final SortedList<Button> sortedRightMemberButtons = new SortedList<>(rightMemberButtons,
            Comparator.comparing(Button::getText, String.CASE_INSENSITIVE_ORDER));

    private ObservableList<MemberOverviewDetails> rightMemberDetails =
            FXCollections.observableArrayList();

    public SplitCenterController(StyleManager styleManager,
                                 ControllerUtilities controllerUtilities,
                                 SceneEngine sceneEngine,
                                 TabManager tabManager,
                                 AppContext appContext, MemberService memberService, SceneConfigurationLoader sceneConfigurationLoader) {
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
        this.sceneEngine = sceneEngine;
        this.tabManager = tabManager;
        this.appContext = appContext;
        this.memberService = memberService;
        this.sceneConfigurationLoader = sceneConfigurationLoader;
    }

    @FXML
    public void initialize() {
        log.info("SplitCenterController initialized");

        controllerUtilities.initializeSceneStyle(splitPane, this);

        setDividers();
        setContentPaneMinWidth();

        rightMemberDetails = memberService.getMemberOverviewDetails();

        sortedRightMemberButtons.addListener((ListChangeListener<Button>)
                change -> {
                    rightMemberTabs.getChildren().setAll(sortedRightMemberButtons);
                    changeStyle();
                });

        tabManager.createGroupScenes(GroupOverViewEnum.OVERVIEW, resources);
        // Whenever the sorted list changes, update the VBox children
        for (MemberOverviewDetails details : rightMemberDetails) {
            tabManager.createMemberScenes(SceneType.MEMBER_OVERVIEW, details);
        }

        log.info("rightMemberTabs.getChildren().size(){}", rightMemberTabs.getChildren().size());

        Platform.runLater(() -> {
            if (!leftGroupTabs.getChildren().isEmpty()) {
                Button firstButton = (Button) leftGroupTabs.getChildren().getFirst();
                firstButton.fire();  // triggers the button’s setOnAction
            }
        });

        styleSidebar(leftGroupTabs);
        styleSidebar(rightMemberTabs);
        styleContent(leftGroupContent);
        styleContent(rightMemberContent);
    }

    // --- Apply to sidebars (tab containers) ---
    public static void styleSidebar(VBox sidebar) {
        sidebar.setStyle(
                "-fx-background-color: #bfbfbf;" // medium-light gray
        );
    }

    // --- Apply to content panes ---
    public static void styleContent(StackPane contentPane) {
        contentPane.setStyle(
                "-fx-background-color: #f8f8f8;" // lighter gray / white
        );
    }

    private void changeStyle() {
        Platform.runLater(() -> {
            splitPane.getScene().getRoot().applyCss();
            splitPane.getScene().getRoot().layout();
        });

    }

    public void addButtonsToLeftGroupTabs(Button button) {
        if (leftGroupTabs.getChildren().contains(button)) {
            log.warn("Button already exists in leftGroupTabs: {}", button.getId());
            return;
        }
        leftGroupTabs.getChildren().add(button);
        setButtonAction(button, true); // true for left side
    }

    public void addButtonsToRightMemberTabs(Button button) {
        if (rightMemberButtons.contains(button)) {
            log.warn("Button already exists in rightMemberButtons: {}", button.getId());
            return;
        }
        rightMemberButtons.add(button);
        setButtonAction(button, false); // false for right side

    }

    public void addSceneRootToLeftGroupContent(Parent sceneRoot) {
        if (leftGroupContent.getChildren().contains(sceneRoot)) {
            log.warn("Scene root already exists in leftGroupContent: {}", sceneRoot.getId());
            return;
        }
        leftGroupContent.getChildren().add(sceneRoot);
        leftContentMap.put(sceneRoot.getId(), sceneRoot);
    }

    public void addSceneRootToRightMemberContent(Parent sceneRoot) {
        if (rightMemberContent.getChildren().contains(sceneRoot)) {
            log.warn("Scene root already exists in rightMemberContent: {}", sceneRoot.getId());
            return;
        }
        rightMemberContent.getChildren().add(sceneRoot);
        rightContentMap.put(sceneRoot.getId(), sceneRoot);
    }

    private double getLargestButtonWidth(VBox buttonContainer) {
        // Ensure the layout is up to date
        buttonContainer.applyCss();
        buttonContainer.layout();

        return buttonContainer.getChildren().stream()
                .filter(node -> node instanceof Button)
                .mapToDouble(node -> ((Button) node).prefWidth(-1)) // -1 for computed pref width
                .max()
                .orElse(0);
    }

    private void adjustDividersForRight() {
        double largestButtonWidth = getLargestButtonWidth(rightMemberTabs);
        double totalWidth = splitPane.getWidth();

        double extraPadding = 0.046 * largestButtonWidth;
        // Compute ratio of space to give right side (add some padding)
        double rightRatio = (largestButtonWidth + extraPadding) / totalWidth;

        // Animate dividers
        animateDividerPosition(leftDivider, 0.05);  // tweak padding
        animateDividerPosition(centerDivider, 0.05);  // rest of the pane
        animateDividerPosition(rightDivider, 1 - rightRatio);
    }

    private void adjustDividersForLeft() {
        double largestButtonWidth = getLargestButtonWidth(leftGroupTabs);
        double totalWidth = splitPane.getWidth();

        double extraPadding = 0.046 * largestButtonWidth;
        double leftRatio = (largestButtonWidth + extraPadding) / totalWidth;

        animateDividerPosition(leftDivider, leftRatio);
        animateDividerPosition(centerDivider, 0.95);
        animateDividerPosition(rightDivider, 0.95);
    }

    private void setDividers() {
        leftDivider = splitPane.getDividers().getFirst();
        centerDivider = splitPane.getDividers().get(1);
        rightDivider = splitPane.getDividers().getLast();

        splitPane.lookupAll(".split-pane-divider").forEach(divider -> {
            divider.setStyle(
                    "-fx-background-color: #ccc;" + // divider color
                            "-fx-padding: 0;" +
                            "-fx-background-insets: 0;" +
                            "-fx-pref-width: 3px;" +        // make it visually thin
                            "-fx-min-width: 3px;" +
                            "-fx-max-width: 3px;"
            );
        });
    }

    private void setContentPaneMinWidth() {
        leftGroupContent.setMinWidth(0);
        rightMemberContent.setMinWidth(0);
    }

    private void animateDividerPosition(SplitPane.Divider divider, double target) {
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

    // --- Base inactive + hover style for side tabs ---
    // --- Apply to side tab buttons ---
    public static void styleSideTab(Button button, boolean isLeftSide) {
        String alignment = isLeftSide ? "CENTER_RIGHT" : "CENTER_LEFT";
        String borderRadius = isLeftSide ?  "10 0 0 10" : "0 10 10 0"; // right-rounded if left, left-rounded if right

        button.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #333333;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 12 8 12;" +
                        "-fx-background-radius: " + borderRadius + ";" +
                        "-fx-border-radius: " + borderRadius + ";" +
                        "-fx-border-color: transparent;" +
                        "-fx-border-width: 0 0 2 0;" +
                        "-fx-cursor: hand;" +
                        "-fx-alignment: " + alignment + ";"
        );

        // Hover effect
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: #e6e6e6;" +
                        "-fx-text-fill: #333333;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 12 8 12;" +
                        "-fx-background-radius: " + borderRadius + ";" +
                        "-fx-border-radius: " + borderRadius + ";" +
                        "-fx-border-color: transparent;" +
                        "-fx-border-width: 0 0 2 0;" +
                        "-fx-cursor: hand;" +
                        "-fx-alignment: " + alignment + ";"
        ));

        // Reset on exit
        button.setOnMouseExited(e -> styleSideTab(button, isLeftSide));
    }

    // --- Active tab style ---
    public static void setActiveTab(Button button, boolean isLeftSide) {
        button.getStyleClass().add("active-tab"); // mark active

        String alignment = isLeftSide ? "CENTER_RIGHT" : "CENTER_LEFT";
        String borderRadius = isLeftSide ?  "10 0 0 10" : "0 10 10 0"; // right-rounded if left, left-rounded if right

        button.setStyle(
                "-fx-background-color: #ffffff;" +       // active = white
                        "-fx-text-fill: #333333;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 12 8 12;" +
                        "-fx-background-radius: " + borderRadius + ";" +
                        "-fx-border-radius: " + borderRadius + ";" +
                        "-fx-border-color: rgba(66,133,244,0.14);" +
                        "-fx-cursor: hand;" +
                        "-fx-alignment: " + alignment + ";"
        );
    }


    // --- Reset all side tabs to inactive ---
    private void resetAllTabs() {
        leftGroupTabs.getChildren().forEach(node -> {
            if (node instanceof Button b) {
                b.getStyleClass().remove("active-tab");
                styleSideTab(b, true); // true for left side
            }
        });
        rightMemberTabs.getChildren().forEach(node -> {
            if (node instanceof Button b) {
                b.getStyleClass().remove("active-tab");
                styleSideTab(b, false); // false for right side
            }
        });
    }

    // --- Apply to each button automatically when added ---
    public void setButtonAction(Button button, boolean isLeftSide) {
        styleSideTab(button, isLeftSide); // always apply base styling when added

        button.setOnAction(event -> {
            resetAllTabs();     // clear all active states
            setActiveTab(button, isLeftSide); // make this one active

            String id = button.getId();

            Parent sceneRoot = leftContentMap.get(id);
            if (sceneRoot != null) {
                sceneRoot.toFront();
                adjustDividersForLeft();
                return;
            }

            sceneRoot = rightContentMap.get(id);
            if (sceneRoot != null) {
                sceneRoot.toFront();
                adjustDividersForRight();
                return;
            }

            log.warn("No scene found for button with id: {}", id);
        });
    }


    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(splitPane.getScene(), this);
    }
}
