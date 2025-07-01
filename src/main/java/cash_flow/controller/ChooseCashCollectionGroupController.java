package cash_flow.controller;

import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.dto.outgoing.GroupSelectionDetails;
import cash_flow.dto.outgoing.OverseerSelectionDetails;
import cash_flow.dto.outgoing.SelectionParentClass;
import cash_flow.scene.SceneConfigurationLoader;
import cash_flow.scene.SceneEngine;
import cash_flow.scene.SceneType;
import cash_flow.service.GroupService;
import cash_flow.service.OverseerService;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChooseCashCollectionGroupController implements ThemeChangeListener {

    private final SceneEngine sceneEngine;
    private final SceneConfigurationLoader sceneConfigurationLoader;
    private final StyleManager styleManager;
    private final ControllerUtilities controllerUtilities;
    private final GroupService groupService;
    private final OverseerService overseerService;

    @FXML
    public Label cashCollectionGroupLabel;

    @FXML
    public ListView<SelectionParentClass> cashCollectionGroupListView;

    @FXML
    public Label groupOverseer;

    @FXML
    public ComboBox<SelectionParentClass> groupOverseerBox;

    @FXML
    public Button addNewOverseerButton;

    // List of all available groups to show for a selected overseer
    private ObservableList<SelectionParentClass> cashCollectionGroupObservableList;

    // List of all overseers to select from
    private ObservableList<SelectionParentClass> overseerSelectionDetailsObservableList;

    @Autowired
    public ChooseCashCollectionGroupController(SceneEngine sceneEngine, SceneConfigurationLoader sceneConfigurationLoader, StyleManager styleManager, ControllerUtilities controllerUtilities, GroupService groupService, OverseerService overseerService) {
        this.sceneEngine = sceneEngine;
        this.sceneConfigurationLoader = sceneConfigurationLoader;
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
        this.groupService = groupService;
        this.overseerService = overseerService;
    }

    @FXML
    public void initialize() {
        controllerUtilities.initializeSceneStyle(cashCollectionGroupLabel, this);

        // Initialize observable lists
        cashCollectionGroupObservableList = FXCollections.observableArrayList();
        overseerSelectionDetailsObservableList = FXCollections.observableArrayList();

        OverseerSelectionDetails overseerSelectionDetails = new OverseerSelectionDetails();
        overseerSelectionDetails.setId(1L); // dummy ID
        overseerSelectionDetails.setName("Példa Felügyelő"); // "Example Overseer"

        // TODO - Load overseer details from the service
        overseerSelectionDetailsObservableList.add(overseerSelectionDetails); // placeholder
        overseerSelectionDetailsObservableList.addAll(overseerService.getOverSeerList());


        // Customize how each overseer appears in the ComboBox dropdown
        groupOverseerBox.setCellFactory(comboBox -> new ListCell<>() {
            @Override
            protected void updateItem(SelectionParentClass item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        // Customize the selected item display (outside of dropdown)
        groupOverseerBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(SelectionParentClass item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        groupOverseerBox.setItems(overseerSelectionDetailsObservableList);

        // Handle ComboBox selection change
        groupOverseerBox.setOnAction(event -> {
            OverseerSelectionDetails selectedOverseer =
                    (OverseerSelectionDetails) groupOverseerBox.getSelectionModel().getSelectedItem();

            if (selectedOverseer != null) {
                loadOverseerGroups(); // Load groups for selected overseer
                // TODO - Handle the selection of an overseer
            } else {
                // Fallback: select first overseer if none selected
                groupOverseerBox.getSelectionModel().selectFirst();
                selectedOverseer = (OverseerSelectionDetails) groupOverseerBox.getSelectionModel().getSelectedItem();
                loadOverseerGroups();
                // TODO - Handle the case where no overseer is selected
            }
        });

        // Attach ListView click handler once
        cashCollectionGroupListView.setOnMouseClicked(this::handleCashCollectionGroupListViewClick);
    }

    /**
     * Load groups associated with the selected overseer into the ListView.
     */
    private void loadOverseerGroups() {
        // TODO - Load new overseer from service

        // Create and add a special "Add New" group item
        GroupSelectionDetails groupSelectionDetails = new GroupSelectionDetails();
        groupSelectionDetails.setId(1L); // dummy ID
        groupSelectionDetails.setName("Új csoport"); // "New group"
        groupSelectionDetails.setDescription("Új csoport létrehozása"); // "Create new group"

        // Clear previous list before adding
        cashCollectionGroupListView.getItems().clear();

        // Add "Add New" pseudo-item
        cashCollectionGroupListView.getItems().add(groupSelectionDetails);

        // Add real group items
        cashCollectionGroupListView.getItems().addAll(cashCollectionGroupObservableList);

        // Set custom cell factory for consistent cell rendering
        cashCollectionGroupListView.setCellFactory(
                listView -> new ListCellController
                        (sceneEngine, sceneConfigurationLoader, styleManager, controllerUtilities)
        );

        // Reset selection and add styling
        cashCollectionGroupListView.getSelectionModel().clearSelection();
        cashCollectionGroupListView.getStyleClass().add("list-view");
    }

    /**
     * Handles mouse clicks on the ListView items.
     * Shows the selected group or reacts to "Add New" item.
     */
    @FXML
    public void handleCashCollectionGroupListViewClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            GroupSelectionDetails selectedItem =
                    (GroupSelectionDetails) cashCollectionGroupListView.getSelectionModel().getSelectedItem();

            if (selectedItem != null && !"Új csoport".equals(selectedItem.getName())) {
                // Handle regular group selection
                System.out.println("Selected Cash Collection Group: " + selectedItem);

            } else if (selectedItem != null) {
                // Handle the "Add New" item
                System.out.println("Add New Cash Collection Group clicked");
                sceneEngine.switchScene(SceneType.CREATE_GROUP);
                // TODO - Trigger group creation dialog or workflow
            }
        }
    }

    /**
     * Handles clicks on the "Add New Overseer" button.
     */
    @FXML
    public void handleAddNewOverseerButtonClick(ActionEvent actionEvent) {
        // TODO - Handle creation of new overseer
        if (actionEvent.getSource() == addNewOverseerButton) {
            System.out.println("Add New Overseer button clicked");
            // TODO - Open dialog or new scene for overseer creation
            sceneEngine.switchScene(SceneType.ADD_GROUP_OVERSEER);
        }
    }

    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(cashCollectionGroupListView.getScene(), this);
    }
}
