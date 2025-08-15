package cash_flow.controller;

import cash_flow.context.AppContext;
import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.scene.SceneEngine;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
@Slf4j
public class GroupOverviewController  implements ThemeChangeListener {

    private final StyleManager styleManager;
    private final ControllerUtilities controllerUtilities;
    private final SceneEngine sceneEngine;
    private final AppContext appContext;

    // ==================== Name FXML components ====================
    @FXML public StackPane nameStackPane;
    @FXML public Label nameLabel;
    @FXML public Label nameValue;
    @FXML public TextField nameTextField;
    @FXML public StackPane nameButtonStackPane;
    @FXML public Button editNameButton;
    @FXML public Button saveNameButton;

    // ==================== Description FXML components ====================
    @FXML public StackPane descriptionStackPane;
    @FXML public Label descriptionLabel;
    @FXML public Label descriptionValue;
    @FXML public TextField descriptionTextField;
    @FXML public StackPane descriptionButtonStackPane;
    @FXML public Button editDescriptionButton;
    @FXML public Button saveDescriptionButton;

    // ==================== Start Date FXML components ====================
    @FXML public Label startDateLabel;
    @FXML public Label startDateValue;

    // ==================== End Date FXML components ====================
    @FXML public Label endDateLabel;
    @FXML public Label endDateValue;

    // ==================== Overseer Name FXML components ====================
    @FXML public Label overseerNameLabel;
    @FXML public Label overseerNameValue;

    // ==================== Number of Members FXML components ====================
    @FXML public Label numberOfMembersLabel;
    @FXML public Label numberOfMembersValue;

    // ==================== Total Payment FXML components ====================
    @FXML public Label totalPaymentLabel;
    @FXML public Label totalPaymentValue;

    // ==================== Current Amount of Money FXML components ====================
    @FXML public Label currentAmountOfMoney;
    @FXML public Label currentAmountOfMoneyValue;

    // ==================== General Resources ====================
    @FXML public ResourceBundle resources;


    public GroupOverviewController(StyleManager styleManager,
                                   ControllerUtilities controllerUtilities,
                                   SceneEngine sceneEngine,
                                   AppContext appContext) {
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
        this.sceneEngine = sceneEngine;
        this.appContext = appContext;
    }

    @FXML
    public void initialize() {
        controllerUtilities.initializeSceneStyle(nameLabel, this);

        // create Overview Scene

    }

    public void onEditNameClicked(ActionEvent actionEvent) {

    }

    public void onSaveNameClicked(ActionEvent actionEvent) {

    }

    public void onEditDescriptionClicked(ActionEvent actionEvent) {
    }

    public void onSaveDescriptionClicked(ActionEvent actionEvent) {

    }

    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(nameLabel.getScene(), this);
    }


}
