package cash_flow.controller;

import cash_flow.scene.SceneEngine;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import lombok.NoArgsConstructor;

public class ListCellController extends ListCell<String> implements SceneComponent{

    @FXML private HBox listCellContainer;
    @FXML private Button addNewCollectionGroupButton;

    private final SceneEngine sceneEngine;

    public ListCellController(SceneEngine sceneEngine) {
        this.sceneEngine = sceneEngine;
        FXMLLoader loader = sceneEngine.createSceneComponent("list_cell_scene");
        loader.setController(this);

        try {
            loader.load();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load ListCellController FXML", e);
        }

        addNewCollectionGroupButton.setOnAction(e -> System.out.println("Add New Collection Group clicked!"));
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
        } else if (item.equals("$$ADD_NEW$$")) { // special placeholder
            setText(null);
            setGraphic(listCellContainer);
        } else {
            setText(item); // Normal item
            setGraphic(null);
        }
    }
}
