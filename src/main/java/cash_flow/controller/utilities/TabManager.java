package cash_flow.controller.utilities;

import cash_flow.application.Utf8Control;
import cash_flow.context.AppContext;
import cash_flow.controller.MemberOverviewController;
import cash_flow.controller.SplitCenterController;
import cash_flow.dto.outgoing.MemberOverviewDetails;
import cash_flow.scene.SceneConfiguration;
import cash_flow.scene.SceneConfigurationLoader;
import cash_flow.scene.SceneEngine;
import cash_flow.scene.SceneType;
import cash_flow.style_manager.StyleManager;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@Slf4j
public class TabManager {

    private final SceneEngine sceneEngine;
    private final SplitCenterController splitCenterController;
    private final SceneConfigurationLoader sceneConfigurationLoader;
    private final SceneConfiguration sceneConfiguration;
    private final StyleManager styleManager;
    private final ControllerUtilities controllerUtilities;
    private final AppContext appContext;


    public TabManager(SceneEngine sceneEngine,
                      @Lazy SplitCenterController splitCenterController, SceneConfigurationLoader sceneConfigurationLoader, SceneConfiguration sceneConfiguration,
                      StyleManager styleManager,
                      ControllerUtilities controllerUtilities,
                      AppContext appContext) {
        this.sceneEngine = sceneEngine;
        this.splitCenterController = splitCenterController;
        this.sceneConfigurationLoader = sceneConfigurationLoader;
        this.sceneConfiguration = sceneConfiguration;
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
        this.appContext = appContext;
    }

    public void createGroupScenes(GroupOverViewEnum groupOverViewEnum, ResourceBundle resources) {
        Scene scene = sceneEngine.getScene(groupOverViewEnum.getSceneType());

        Button button = new Button(
                resources.getString(groupOverViewEnum.getButtonMessagesId())
        );
        button.setId(groupOverViewEnum.getId());

        Parent root = scene.getRoot();
        root.setId(groupOverViewEnum.getId());
        splitCenterController.addSceneRootToLeftGroupContent(root);
        splitCenterController.addButtonsToLeftGroupTabs(button);
    }

    public void createMemberScenes(SceneType memberOverview, MemberOverviewDetails details) {
        try {
            sceneConfigurationLoader.load(memberOverview);
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(sceneConfiguration.getFxml()));
            ResourceBundle resourceBundle =
                    ResourceBundle.getBundle(sceneConfiguration.getMessages(), new Utf8Control());
            loader.setResources(resourceBundle);
            Parent sceneRoot = loader.load();
            // Access the controller
            MemberOverviewController controller = loader.getController();
            // Set the necessary dependencies
            controller.setStyleManager(styleManager);
            controller.setControllerUtilities(controllerUtilities);
            controller.setSceneEngine(sceneEngine);
            controller.setAppContext(appContext);
            controller.setMemberOverviewDetails(details);
            // Set the scene root
            sceneRoot.setId(details.getId());

            // Optionally, set up buttons if needed
            Button tabButton = new Button(details.getName());
            tabButton.setId(details.getId());
            splitCenterController.addButtonsToRightMemberTabs(tabButton);

            // Add the scene root to your right content
            splitCenterController.addSceneRootToRightMemberContent(sceneRoot);

        } catch (IOException e) {
            log.error("Failed to load member scene", e);
        }
    }

//    // --- Apply to side tab buttons ---
//    public static void styleSideTab(Button button) {
//        button.setStyle(
//                "-fx-background-color: transparent;" +
//                        "-fx-text-fill: #333333;" +        // dark text
//                        "-fx-font-weight: bold;" +
//                        "-fx-padding: 8 12 8 12;" +        // top, right, bottom, left
//                        "-fx-background-radius: 6 6 0 0;" +
//                        "-fx-border-radius: 6 6 0 0;" +
//                        "-fx-border-color: transparent;" +
//                        "-fx-border-width: 0 0 2 0;" +     // underline space
//                        "-fx-cursor: hand;" +
//                        "-fx-alignment: center-left;"
//        );
//
//        // You can also hook hover effects in code:
//        button.setOnMouseEntered(e -> button.setStyle(
//                "-fx-background-color: #e6e6e6;" +
//                        "-fx-text-fill: #333333;" +
//                        "-fx-font-weight: bold;" +
//                        "-fx-padding: 8 12 8 12;" +
//                        "-fx-background-radius: 6 6 0 0;" +
//                        "-fx-border-radius: 6 6 0 0;" +
//                        "-fx-border-color: transparent;" +
//                        "-fx-border-width: 0 0 2 0;" +
//                        "-fx-cursor: hand;" +
//                        "-fx-alignment: center-left;"
//        ));
//        button.setOnMouseExited(e -> styleSideTab(button)); // reset on exit
//    }
//
//    // --- Optional: active tab style ---
//    public static void setActiveTab(Button button) {
//        button.setStyle(
//                "-fx-background-color: #ffffff;" +  // active = white
//                        "-fx-text-fill: #333333;" +
//                        "-fx-font-weight: bold;" +
//                        "-fx-padding: 8 12 8 12;" +
//                        "-fx-background-radius: 6 6 0 0;" +
//                        "-fx-border-radius: 6 6 0 0;" +
//                        "-fx-border-color: #4285f4;" +     // Chrome-blue underline
//                        "-fx-border-width: 0 0 2 0;" +
//                        "-fx-cursor: hand;" +
//                        "-fx-alignment: center-left;"
//        );
//    }

}
