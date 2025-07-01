package cash_flow.controller;

import cash_flow.common.StatusResponses;
import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.dto.incoming.OverseerCreationCommand;
import cash_flow.dto.outgoing.OverseerSelectionDetails;
import cash_flow.service.OverseerService;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AddGroupOverseerScene implements ThemeChangeListener {

    @FXML
    public TextField firstName;
    @FXML
    public TextField lastName;
    @FXML
    public TextField email;
    @FXML
    public Label systemResponseLabel;
    @FXML
    public Button createNewOverseer;

    private final StyleManager styleManager;
    private final ControllerUtilities controllerUtilities;
    private final OverseerService overseerService;
    private final ChooseCashCollectionGroupController chooseCashCollectionGroupController;

    public AddGroupOverseerScene(StyleManager styleManager, ControllerUtilities controllerUtilities, OverseerService overseerService, ChooseCashCollectionGroupController chooseCashCollectionGroupController) {
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
        this.overseerService = overseerService;
        this.chooseCashCollectionGroupController = chooseCashCollectionGroupController;
    }

    @FXML
    public void initialize() {
        log.info("AddGroupOverseerScene initialized");

        controllerUtilities.initializeSceneStyle(createNewOverseer, this);
    }

    @FXML
    public void handleCreateNewOverseer(ActionEvent actionEvent) {
        log.info("Creating new overseer with details: firstName: {}; lastName: {}; email: {}",
                firstName.getText(), lastName.getText(), email.getText());

        OverseerCreationCommand command = new OverseerCreationCommand(
                firstName.getText(),
                lastName.getText(),
                email.getText());

        StatusResponses creationStatus = overseerService.createNewOverseer(command);
        systemResponseLabel.setText(creationStatus.getMessage());
        if(creationStatus.equals(StatusResponses.SUCCESS)) {
            log.info("Overseer created successfully, updating overseer list in ChooseCashCollectionGroupController");

            Stage stage = (Stage) createNewOverseer.getScene().getWindow();
            stage.close();
        }
    }

    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(createNewOverseer.getScene(), this);
    }
}
