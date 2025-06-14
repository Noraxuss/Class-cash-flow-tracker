package cash_flow.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
public class ChooseCashCollectionGroupController {

    @FXML
    public Label cashCollectionGroupLabel;
    @FXML
    public ListView<String> cashCollectionGroupListView;

    private ResourceBundle resources;

    @FXML
    public void initialize() {
        cashCollectionGroupListView.setCellFactory(listView ->
                new ListCellController());

        // TEMP EXAMPLE DATA
        cashCollectionGroupListView.getItems().addAll("Group A", "Group B", "Group C");
    }
}
