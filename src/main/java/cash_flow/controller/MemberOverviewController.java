package cash_flow.controller;

import cash_flow.context.AppContext;
import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.dto.outgoing.MemberOverviewDetails;
import cash_flow.scene.SceneEngine;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberOverviewController implements ThemeChangeListener {

    @Setter private StyleManager styleManager = null;
    private ControllerUtilities controllerUtilities = null;
    @Setter private SceneEngine sceneEngine = null;
    @Setter private AppContext appContext = null;

    // ==================== Name FXML components ====================
    @FXML public StackPane nameStackPane;
    @FXML public Label nameLabel;
    @FXML public Label nameValue;
    @FXML public TextField nameTextField;
    @FXML public StackPane buttonStackPane;
    @FXML public Button editNameButton;
    @FXML public Button saveNameButton;

    // ==================== E-mail FXML components ====================
    @FXML public Label emailLabel;
    @FXML public Label emailValue;

    // ==================== Group Join Date FXML components ====================
    @FXML public Label groupJoinDateLabel;
    @FXML public Label groupJoinDateValue;

    // ==================== Group Leave Date FXML components ====================
    @FXML public Label groupLeaveDateLabel;
    @FXML public Label groupLeaveDateValue;

    // ==================== Group Memberships FXML components ====================
    @FXML public Label groupMembershipsLabel;

    // ==================== Total Payment FXML components ====================
    @FXML public Label totalPaymentLabel;
    @FXML public Label totalPaymentValue;

    private MemberOverviewDetails memberOverviewDetails;

    @FXML
    public void initialize() {

    }

    public void onEditNameClicked(ActionEvent actionEvent) {

    }

    public void onSaveEditNameClicked(ActionEvent actionEvent) {

    }

    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(nameLabel.getScene(), this);
    }

    public void setControllerUtilities(ControllerUtilities controllerUtilities) {
        this.controllerUtilities = controllerUtilities;
        controllerUtilities.initializeSceneStyle(nameLabel, this);
    }

    public void setMemberOverviewDetails(MemberOverviewDetails memberOverviewDetails) {
        this.memberOverviewDetails = memberOverviewDetails;
        if (memberOverviewDetails != null) {
            log.info("Setting member overview details: {}", memberOverviewDetails);
            nameValue.setText(memberOverviewDetails.getName());
            emailValue.setText(memberOverviewDetails.getEmail());
            groupJoinDateValue.setText(memberOverviewDetails.getGroupJoinDate().toString());
            groupLeaveDateValue.setText(memberOverviewDetails.getGroupLeaveDate() != null ?
                    memberOverviewDetails.getGroupLeaveDate().toString() : "N/A");
//            groupMembershipsLabel.setText(String.valueOf(memberOverviewDetails.getGroupMemberships().size()));
            totalPaymentValue.setText(String.valueOf(memberOverviewDetails.getTotalPayment()));
        } else {
            log.warn("Member overview details are null");
        }
    }

}
