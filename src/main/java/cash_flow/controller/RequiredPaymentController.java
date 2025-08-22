package cash_flow.controller;

import cash_flow.context.AppContext;
import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.controller.utilities.TabManager;
import cash_flow.scene.SceneEngine;
import cash_flow.scene.SceneType;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Window;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Month;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class RequiredPaymentController implements ThemeChangeListener {

    private final StyleManager styleManager;
    private final ControllerUtilities controllerUtilities;
    private final SceneEngine sceneEngine;
    private final TabManager tabManager;
    private final AppContext appContext;
    public HBox nameHBox;
    public Label nameLabel;
    public RadioButton singlePaymentRadioButton;
    public RadioButton recurringPaymentsRadioButton;
    public RadioButton multiInstallmentPaymentsRadioButton;
    public VBox singlePaymentHBox;
    public Label singlePaymentAmountLabel;
    public TextField singlePaymentTextField;
    public VBox recurringPaymentsGridPane;
    public VBox MultiInstallmentPayments;
    public Button addRow;
    public Button removeRow;
    public ScrollPane multipleInstallmentPaymentsGridPane;
    public Label singlePaymentDueDateLabel;
    public DatePicker singlePaymentDueDatePicker;
    public Label singlePaymentExemptionsLabel;
    public Button singlePaymentExemptionsButton;
    public Label singlePaymentCurrencyLabel;
    public ComboBox singlePaymentCurrencyComboBox;
    public Label recurringPaymentAmountLabel;
    public TextField recurringPaymentAmountTextField;
    public Label recurringPaymentCurrencyLabel;
    public ComboBox recurringPaymentCurrencyComboBox;
    public Label recurringPaymentExemptionsLabel;
    public Button recurringPaymentExemptionsButton;
    public Button saveButton;
    public Button cancelButton;


    // FXML-injected nodes
    @FXML private TextField nameTextField;
    @FXML private CheckBox isRecurringPaymentCheckBox;
    @FXML private CheckBox isMultipleInstallmentsCheckBox;

    // Month checkboxes
    @FXML public GridPane monthCheckBox;
    @FXML private CheckBox march;
    @FXML private CheckBox april;
    @FXML private CheckBox may;
    @FXML private CheckBox june;
    @FXML private CheckBox july;
    @FXML private CheckBox august;
    @FXML private CheckBox september;
    @FXML private CheckBox october;
    @FXML private CheckBox november;
    @FXML private CheckBox december;
    @FXML private CheckBox january;
    @FXML private CheckBox february;

    // Store month-checkbox mapping for easy lookups
    private Map<Month, CheckBox> monthCheckBoxMap;

    public RequiredPaymentController(StyleManager styleManager,
                                     ControllerUtilities controllerUtilities,
                                     SceneEngine sceneEngine,
                                     TabManager tabManager,
                                     AppContext appContext) {
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
        this.sceneEngine = sceneEngine;
        this.tabManager = tabManager;
        this.appContext = appContext;
    }

    @FXML
    public void initialize() {
        log.info("Initializing RequiredPaymentController");

        controllerUtilities.initializeSceneStyle(nameTextField, this);
    }

    public void onAddRowClicked(MouseEvent mouseEvent) {

    }

    public void onRemoveRowClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(nameTextField.getScene(), this);
    }

    public void onExemptionsButtonClicked(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = sceneEngine.createSceneComponent(SceneType.MEMBER_EXEMPTION);
            Parent root = loader.getRoot();

            MemberExemptionsController memberExemptionsController = loader.getController();

            Popup popup = new Popup();
            popup.getContent().add(root);
            popup.setAutoHide(true);

            // Attach popup to the clicked button
            Node clickedNode = (Node) mouseEvent.getSource();
            Window ownerWindow = clickedNode.getScene().getWindow();

            double x = clickedNode.localToScreen(0, 0).getX();
            double y = clickedNode.localToScreen(0, 0).getY() + clickedNode.getBoundsInParent().getHeight();

            popup.show(ownerWindow, x, y);

            popup.setOnHidden(event -> {
                log.info("Popup closed");
                // ✅ Get all selected members when popup closes
                List<String> selectedMembers = memberExemptionsController.exemptionsVBox.getChildren().stream()
                        .filter(node -> node instanceof CheckBox checkBox && checkBox.isSelected())
                        .map(node -> ((CheckBox) node).getText())
                        .toList();

                log.info("Selected members: {}", selectedMembers);
            });

        } catch (Exception e) {
            log.error("Error showing popup", e);
        }
    }


    public void onSaveButtonClicked(MouseEvent mouseEvent) {

    }

    public void onCancelButtonClicked(MouseEvent mouseEvent) {

    }
}
