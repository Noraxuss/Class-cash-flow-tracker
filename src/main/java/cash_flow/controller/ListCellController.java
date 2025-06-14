package cash_flow.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import java.io.IOException;

public class ListCellController extends ListCell<String> {

    @FXML private HBox listCellContainer;
    @FXML private Button addNewCollectionGroupButton;

    private FXMLLoader loader;

    public ListCellController() {
        addNewCollectionGroupButton.setOnAction(e -> {
            System.out.println("Button clicked for item: " + getItem());
            // do logic here
        });
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
        } else {
            setText(null); // don’t show default text
            setGraphic(listCellContainer);
        }
    }
}
