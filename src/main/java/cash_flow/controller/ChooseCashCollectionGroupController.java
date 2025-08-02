package cash_flow.controller;

import cash_flow.context.AppContext;
import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.controller.utilities.ListCellController;
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
    private final AppContext appContext;

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

    @FXML
    public Label systemResponseLabel;

    // List of all available groups to show for a selected overseer
    private ObservableList<SelectionParentClass> cashCollectionGroupObservableList;

    // List of all overseers to select from
    private ObservableList<SelectionParentClass> overseerSelectionDetailsObservableList;

    @Autowired
    public ChooseCashCollectionGroupController(SceneEngine sceneEngine, SceneConfigurationLoader sceneConfigurationLoader, StyleManager styleManager, ControllerUtilities controllerUtilities, GroupService groupService, OverseerService overseerService, AppContext appContext) {
        this.sceneEngine = sceneEngine;
        this.sceneConfigurationLoader = sceneConfigurationLoader;
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
        this.groupService = groupService;
        this.overseerService = overseerService;
        this.appContext = appContext;
    }

    /**
     * Initializes the controller after the FXML has been loaded.
     * This method sets up the scene style, loads overseers into the ComboBox,
     * and configures event handlers for user interactions.
     */
    @FXML
    public void initialize() {
        controllerUtilities.initializeSceneStyle(cashCollectionGroupLabel, this);

        // Initialize observable lists
        cashCollectionGroupObservableList = FXCollections.observableArrayList();
        overseerSelectionDetailsObservableList = FXCollections.observableArrayList();

        // Load overseers into the ComboBox
        Platform.runLater(() -> {
            Stage stage = (Stage) cashCollectionGroupListView.getScene().getWindow();
            loadCombobox();
            stage.focusedProperty().addListener((obs, oldFocus, newFocus) -> {
                if (newFocus.equals(true)) {
                    log.info("Stage focused, reloading overseer list and groups.");
                    // Reload overseers and groups when the stage gains focus
                    loadCombobox();
                }
            });
        });

        setupCombobox();

        // Handle ComboBox selection change
        groupOverseerBox.setOnAction(event -> {
            OverseerSelectionDetails selectedOverseer =
                    (OverseerSelectionDetails) groupOverseerBox.getSelectionModel().getSelectedItem();

            if (selectedOverseer != null) {
                loadOverseerGroups(selectedOverseer); // Load groups for selected overseer
                appContext.getOverseerContext().setOverseerId(selectedOverseer.getId());
            } else {
                log.warn("No overseer selected, cannot load groups.");

            }
        });

        // Attach ListView click handler once
        cashCollectionGroupListView.setOnMouseClicked(this::handleCashCollectionGroupListViewClick);

        Platform.runLater(() -> {
            Stage stage = (Stage) cashCollectionGroupListView.getScene().getWindow();
            stage.focusedProperty().addListener((obs, oldFocus, newFocus) -> {
                if (newFocus.equals(true) && appContext.getGroupContext() != null) {
                    sceneEngine.loadingNextScene(SceneType.SPLIT_CENTER);
                    controllerUtilities.closeStage(cashCollectionGroupListView);

                }

            });
        });
        log.info("ChooseCashCollectionGroupController initialized");
    }

    /**
     * Loads overseers into the ComboBox from the overseer service.
     * This method fetches the list of overseers and populates the ComboBox.
     * It should be called during initialization or when overseers are updated.
     */
    private void loadCombobox() {
        overseerSelectionDetailsObservableList.clear();
        overseerSelectionDetailsObservableList.addAll(overseerService.getOverSeerList());
        groupOverseerBox.setItems(overseerSelectionDetailsObservableList); // Load groups for the first overseer by default
    }

    /**
     * Sets up the ComboBox for overseers with custom cell factories.
     * This method customizes how overseers are displayed in the dropdown and selected item area.
     */
    private void setupCombobox() {
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
    }

    /**
     * Load groups associated with the selected overseer into the ListView.
     * This method simulates loading groups from a service.
     * It should be replaced with actual service calls to fetch groups.
     * This method also adds a special "Add New" group item at the top of the list.
     * This item allows users to create a new group directly from the ListView.
     *
     * @param selectedOverseer the overseer whose groups are to be loaded
     */
    private void loadOverseerGroups(OverseerSelectionDetails selectedOverseer) {
        cashCollectionGroupObservableList.clear();
        cashCollectionGroupObservableList.addAll(groupService.getOverseerGroups(selectedOverseer)); // Fetch groups for the selected overseer

        // Clear a previous list before adding
        cashCollectionGroupListView.getItems().clear();

        // Add real group items
        cashCollectionGroupListView.getItems().addAll(cashCollectionGroupObservableList);

        // Set custom cell factory for consistent cell rendering
        cashCollectionGroupListView.setCellFactory(
                listView -> new ListCellController
                        (sceneEngine, sceneConfigurationLoader,
                                styleManager, controllerUtilities)
        );

        // Reset selection and add styling
        cashCollectionGroupListView.getSelectionModel().clearSelection();
        cashCollectionGroupListView.getStyleClass().add("list-view");
    }

    /**
     * Handles mouse clicks on the ListView items.
     * Shows the selected group or reacts to "Add New" item.
     * This method is triggered when the user clicks on a group in the ListView.
     * It checks if the clicked item is a valid group or the "Add New" item.
     * If a valid group is selected, it sets the group ID in the application context
     * and closes the current stage.
     *
     * @param event the MouseEvent triggered by the click
     */
    @FXML
    public void handleCashCollectionGroupListViewClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            GroupSelectionDetails selectedItem =
                    (GroupSelectionDetails) cashCollectionGroupListView
                            .getSelectionModel().getSelectedItem();

            if (selectedItem != null && !"Új csoport".equals(selectedItem.getName())) {
                // Handle regular group selection
                log.info("Selected Cash Collection Group: {}", selectedItem);
                Long groupId = selectedItem.getGroupId();
                appContext.getGroupContext().setGroupId(groupId);
                appContext.getGroupContext().setStartDate(groupService.getGroupStartDate(groupId));
                Stage stage = (Stage) cashCollectionGroupListView.getScene().getWindow();
                stage.close(); // Close the current stage
                sceneEngine.loadingNextScene(SceneType.SPLIT_CENTER);

            } else if (selectedItem != null) {
                // Handle the "Add New" item
                log.info("Add New Cash Collection Group clicked");
                sceneEngine.loadingNextScene(SceneType.CREATE_GROUP);
            }
        }
    }

    /**
     * Handles clicks on the "Add New Overseer" button.
     * Opens a dialog or scene for creating a new overseer.
     *
     * @param actionEvent the ActionEvent triggered by the button click
     */
    @FXML
    public void handleAddNewOverseerButtonClick(ActionEvent actionEvent) {
        if (actionEvent.getSource() == addNewOverseerButton) {
            log.info("Add New Overseer button clicked, switching to AddGroupOverseer scene");
            sceneEngine.loadingNextScene(SceneType.ADD_GROUP_OVERSEER);
        }
    }

    /**
     * Handles clicks on the "Back" button.
     * Switches back to the previous scene.
     */
    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(cashCollectionGroupListView.getScene(), this);
    }
}
