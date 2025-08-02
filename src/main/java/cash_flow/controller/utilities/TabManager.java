package cash_flow.controller.utilities;

import cash_flow.scene.SceneConfiguration;
import cash_flow.scene.SceneConfigurationLoader;
import cash_flow.scene.SceneEngine;
import cash_flow.scene.SceneType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
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
        if (sceneType==SceneType.GROUP_MEMBER_DATA) {
            Label groupMemberLabel = new Label("Member");
            tab.setGraphic(groupMemberLabel);
        } else {
            Label groupLabel = new Label("Overview");
            tab.setGraphic(groupLabel);
        }

        return tab;
    }

}
