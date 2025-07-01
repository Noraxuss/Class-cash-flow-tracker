package cash_flow.controller;

import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.dto.incoming.GroupCreationCommand;
import cash_flow.scene.SceneEngine;
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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreateGroupController implements ThemeChangeListener {

    private final StyleManager styleManager;
    private final SceneEngine sceneEngine;
    private final ControllerUtilities controllerUtilities;

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

    public CreateGroupController(StyleManager styleManager, SceneEngine sceneEngine, ControllerUtilities controllerUtilities) {
        this.styleManager = styleManager;
        this.sceneEngine = sceneEngine;
        this.controllerUtilities = controllerUtilities;
    }

    public void initialize() {
        controllerUtilities.initializeSceneStyle(createGroup, this);
    }

    public void backButtonClicked(MouseEvent mouseEvent) {

    }

    public void createGroupButtonClicked(MouseEvent mouseEvent) {
        String groupName = nameTextField.getText();
        String description = descriptionTextField.getText();
        String creationDateString = creationDate.getValue().toString();
        String endDateString = endDate.getValue().toString();

        GroupCreationCommand groupCreationCommand = new GroupCreationCommand(
                groupName,
                description,
                creationDateString,
                endDateString);
    }

    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(createGroup.getScene(), this);
    }
}
