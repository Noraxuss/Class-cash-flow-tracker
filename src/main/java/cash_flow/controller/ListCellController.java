package cash_flow.controller;

import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.dto.outgoing.SelectionParentClass;
import cash_flow.scene.SceneConfiguration;
import cash_flow.scene.SceneConfigurationLoader;
import cash_flow.scene.SceneEngine;
import cash_flow.scene.SceneType;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class ListCellController extends ListCell<SelectionParentClass>
implements ThemeChangeListener{

    private static final String ADD_NEW = "Új csoport";

    @FXML
    private HBox listCellContainer;

    @FXML
    private Label groupLabel;

    private final SceneEngine sceneEngine;
    private final SceneConfigurationLoader sceneConfigurationLoader;
    private final StyleManager styleManager;
    private final ControllerUtilities controllerUtilities;

    public ListCellController(SceneEngine sceneEngine, SceneConfigurationLoader sceneConfigurationLoader, StyleManager styleManager, ControllerUtilities controllerUtilities) {
        this.sceneEngine = sceneEngine;
        this.sceneConfigurationLoader = sceneConfigurationLoader;
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
        loadFXML();
    }

    @FXML
    public void initialize() {
        log.info("ListCellController initialized");

        controllerUtilities.initializeSceneStyle(listCellContainer, this);
    }

    private void loadFXML() {
        FXMLLoader loader = sceneEngine.createSceneComponent(SceneType.LIST_CELL);
        loader.setController(this);
        try {
            listCellContainer = loader.load(); // listCellContainer is root of FXML

            // Load CSS
            SceneConfiguration sceneConfiguration = sceneConfigurationLoader.load(SceneType.LIST_CELL);
            String css = Objects.requireNonNull(getClass()
                    .getResource(sceneConfiguration.getCssLight())).toExternalForm();
            listCellContainer.getStylesheets().add(css);

            // Ensure full width layout
            listCellContainer.setMaxWidth(Double.MAX_VALUE);
            setMaxWidth(Double.MAX_VALUE);
//            this.getStyleClass().add("custom-cell");

        } catch (IOException e) {
            throw new RuntimeException("Failed to load list cell FXML", e);
        }
    }

    @Override
    protected void updateItem(SelectionParentClass item, boolean empty) {
        // Always call the superclass method first to preserve default behavior
        super.updateItem(item, empty);

        // If the cell is empty or the item is null (e.g., scrolling or resizing), clear it
        if (empty || item == null) {
            setText(null);                 // Clear text content
            setGraphic(null);              // Clear any graphical content (like HBox)
        } else {
            // Check if the current item represents the "Add New" pseudo-option
            if (ADD_NEW.equals(item.getName())) {
                groupLabel.setText("Create New Group");  // Show prompt-like label
            } else {
                groupLabel.setText(item.getName());      // Otherwise, display item's name
            }

            // Remove any custom styles previously added to this cell's container
            listCellContainer.getStyleClass().removeAll("add-new", "hovered");

            // If it's the special "Add New" item, apply a specific style class
            if (item.getName().equals(ADD_NEW)) {
                listCellContainer.getStyleClass().add("add-new");
            }

            // Set up click behavior: choose different actions based on what was clicked
            listCellContainer.setOnMouseClicked(e -> {
                if (ADD_NEW.equals(getItem().getName())) {
                    // If "Add New" was clicked, trigger the creation logic
                    System.out.println("Create New Group Clicked!");
                } else {
                    // If a normal group was clicked, just log its name or act on it
                    System.out.println("Selected group: " + getItem());
                }
            });

            // Ensure the cell displays only the graphic (i.e., the styled HBox), not plain text
            setText(null);
            setGraphic(listCellContainer);
        }
    }

    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(listCellContainer.getScene(), this);
    }
}
