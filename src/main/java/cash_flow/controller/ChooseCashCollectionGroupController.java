package cash_flow.controller;

import cash_flow.scene.SceneEngine;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChooseCashCollectionGroupController {

    @FXML
    public Label cashCollectionGroupLabel;
    @FXML
    public ListView<String> cashCollectionGroupListView;

    private final SceneEngine sceneEngine;

    @Autowired
    public ChooseCashCollectionGroupController(SceneEngine sceneEngine) {
        this.sceneEngine = sceneEngine;
    }

    @FXML
    public void initialize() {
        // Set up cell factory
        cashCollectionGroupListView
                .setCellFactory(listView -> new ListCellController(sceneEngine));

        // Add special first item (used for the button row)
        cashCollectionGroupListView.getItems().add("$$ADD_NEW$$");

        // Add normal items
        cashCollectionGroupListView.getItems().addAll("Group A", "Group B", "Group C");
    }
}
