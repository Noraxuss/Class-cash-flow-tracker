package cash_flow.controller;

import cash_flow.context.AppContext;
import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.dto.incoming.GroupCreationCommand;
import cash_flow.scene.SceneEngine;
import cash_flow.scene.SceneType;
import cash_flow.service.GroupService;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreateGroupController implements ThemeChangeListener {

    private final StyleManager styleManager;
    private final SceneEngine sceneEngine;
    private final ControllerUtilities controllerUtilities;
    private final GroupService groupService;
    private final AppContext appContext;

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private DatePicker creationDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private Button createGroup;
    @FXML
    private Button back;

    public CreateGroupController(StyleManager styleManager, SceneEngine sceneEngine, ControllerUtilities controllerUtilities, GroupService groupService, AppContext appContext) {
        this.styleManager = styleManager;
        this.sceneEngine = sceneEngine;
        this.controllerUtilities = controllerUtilities;
        this.groupService = groupService;
        this.appContext = appContext;
    }

    /**
     * Initializes the CreateGroupController by setting up the scene style.
     */
    public void initialize() {
        controllerUtilities.initializeSceneStyle(createGroup, this);
    }

    /**
     * Handles the creation of a new group when the create button is clicked.
     *
     * @param mouseEvent the mouse event triggered by clicking the create button
     */
    public void createGroupButtonClicked(MouseEvent mouseEvent) {
        String groupName = nameTextField.getText();
        String description = descriptionTextField.getText();
        String creationDateString = creationDate.getValue().toString();
        String endDateString = endDate.getValue().toString();

        GroupCreationCommand groupCreationCommand = new GroupCreationCommand(
                groupName,
                description,
                creationDateString,
                endDateString,
                appContext.getOverseerContext().getOverseerId());

        log.info("Creating group with details: name: {}; description: {}; creationDate: {}; endDate: {}",
                groupName, description, creationDateString, endDateString);

        long response = groupService.createGroup(groupCreationCommand);

        Platform.runLater(() -> {
            appContext.getGroupContext().setGroupId(response);
            backButtonClicked(mouseEvent);
        });
    }

    /**
     * Handles the back button click event to close the current stage.
     *
     * @param mouseEvent the mouse event triggered by clicking the back button
     */
    public void backButtonClicked(MouseEvent mouseEvent) {
        log.info("Back button clicked, returning to the previous scene.");
        Stage stage = (Stage) back.getScene().getWindow();
        stage.close();
    }


    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(createGroup.getScene(), this);
    }
}
