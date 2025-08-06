package cash_flow.controller.utilities;

import cash_flow.scene.SceneConfiguration;
import cash_flow.scene.SceneConfigurationLoader;
import cash_flow.scene.SceneEngine;
import cash_flow.scene.SceneType;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class TabManager {

    private final SceneEngine sceneEngine;
    private final SceneConfigurationLoader sceneConfigurationLoader;

    public TabManager(SceneEngine sceneEngine, SceneConfigurationLoader sceneConfigurationLoader) {
        this.sceneEngine = sceneEngine;
        this.sceneConfigurationLoader = sceneConfigurationLoader;
    }

    public Tab createTab(SceneType sceneType) {
        Tab tab = new Tab();

        FXMLLoader fxmlLoader = sceneEngine.createSceneComponent(sceneType);

        try {
            // Load the root Node from the FXML
            Node root = fxmlLoader.load();
            tab.setContent(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        tab.setClosable(false);
        if (sceneType == SceneType.GROUP_MEMBER_DATA) {
//            Label groupMemberLabel = new Label("Member");
//            groupMemberLabel.setPadding(Insets.EMPTY);            // Remove padding
//            groupMemberLabel.setAlignment(Pos.CENTER);             // Center text inside label
//            groupMemberLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Allow to grow if needed
//            tab.setGraphic(groupMemberLabel);
            tab.setText("Member");

        } else {
            Label groupLabel = new Label("Overview");
            groupLabel.setPadding(Insets.EMPTY);
            groupLabel.setAlignment(Pos.CENTER);
            groupLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            tab.setGraphic(groupLabel);
        }


        return tab;
    }

}
