package cash_flow.controller;

import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.dto.PaymentRowModel;
import cash_flow.dto.PaymentRowModelEnum;
import cash_flow.scene.SceneEngine;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
public class AddPaymentController implements ThemeChangeListener {

    // Dependencies
    private final StyleManager styleManager;
    private final ControllerUtilities controllerUtilities;
    private final SceneEngine sceneEngine;

    // TableView fields
    @FXML
    public TableView<PaymentRowModel> makePayment;
    @FXML
    public TableColumn<PaymentRowModel, String> paymentMakerColumn;
    @FXML
    public TableColumn<PaymentRowModel, LocalDate> paymentDateColumn;
    @FXML
    public TableColumn<PaymentRowModel, String> paymentGoalColumn;
    @FXML
    public TableColumn<PaymentRowModel, Integer> paymentAmountColumn;
    @FXML
    public TableColumn<PaymentRowModel, String> currencyColumn;

    // Button fields
    @FXML
    public HBox buttonsHBox;
    @FXML
    public Button saveChangesButton;
    @FXML
    public Button backButton;

    // List of payment row models
    private ObservableList<PaymentRowModel> paymentRowModels;

    public AddPaymentController(StyleManager styleManager,
                                ControllerUtilities controllerUtilities,
                                SceneEngine sceneEngine) {
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
        this.sceneEngine = sceneEngine;
    }


    @FXML
    public void initialize() {
        log.info("AddPaymentController initialized");

        // Initialize scene style
        controllerUtilities.initializeSceneStyle(buttonsHBox, this);

        configureTable();


    }

    private void configureTable() {
        makePayment.setEditable(true);

//        paymentMaker.setUserData(PaymentRowModelEnum.PAYMENT_MAKER);
//        paymentDate.setUserData(PaymentRowModelEnum.PAYMENT_DATE);
//        paymentGoal.setUserData(PaymentRowModelEnum.PAYMENT_GOAL);
//        paymentAmount.setUserData(PaymentRowModelEnum.PAYMENT_AMOUNT);
//        currency.setUserData(PaymentRowModelEnum.CURRENCY);

        for (TableColumn<PaymentRowModel, ?> column : makePayment.getColumns()) {
            chooseColumnType(column);
        }
    }

    private void chooseColumnType(TableColumn<PaymentRowModel, ?> column) {
//        PaymentRowModelEnum field
    }

    public void backButtonClicked(ActionEvent actionEvent) {
    }

    public void saveChangesClicked(ActionEvent actionEvent) {
    }

    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(buttonsHBox.getScene(), this);

    }
}
