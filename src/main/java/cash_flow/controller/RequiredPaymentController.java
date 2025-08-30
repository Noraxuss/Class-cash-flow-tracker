package cash_flow.controller;

import cash_flow.context.AppContext;
import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.controller.utilities.TabManager;
import cash_flow.dto.incoming.RequiredPaymentCommand;
import cash_flow.dto.outgoing.MemberExemptionDetails;
import cash_flow.repository.RequiredPaymentRepository;
import cash_flow.scene.SceneEngine;
import cash_flow.scene.SceneType;
import cash_flow.service.CurrencyService;
import cash_flow.service.GroupService;
import cash_flow.service.MemberService;
import cash_flow.service.RequiredPaymentService;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static java.util.Map.entry;

@Component
@Slf4j
public class RequiredPaymentController implements ThemeChangeListener {

    // ===========================
// --- SERVICES / CONTEXT ---
// ===========================
    private final StyleManager styleManager;
    private final ControllerUtilities controllerUtilities;
    private final SceneEngine sceneEngine;
    private final TabManager tabManager;
    private final AppContext appContext;
    private final MemberService memberService;
    private final GroupService groupService;
    private final CurrencyService currencyService;
    private final RequiredPaymentService requiredPaymentService;


    // ===========================
// --- MAIN ACTION BUTTONS ---
// ===========================
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    // ===========================
// --- NAME INPUT SECTION ---
// ===========================
    @FXML private HBox nameHBox;
    @FXML private Label nameLabel;
    @FXML private TextField nameTextField;

    // ===========================
// --- PAYMENT TYPE TOGGLES ---
// ===========================
    @FXML private ToggleGroup paymentType;
    @FXML private RadioButton singlePaymentRadioButton;
    @FXML private RadioButton recurringPaymentsRadioButton;
    @FXML private RadioButton multiInstallmentPaymentsRadioButton;

    // ===========================
// --- SINGLE PAYMENT FIELDS ---
// ===========================
    @FXML private VBox singlePaymentHBox;
    @FXML private Label singlePaymentAmountLabel;
    @FXML public Spinner<Integer> singlePaymentSpinner;
    @FXML private Label singlePaymentDueDateLabel;
    @FXML private DatePicker singlePaymentDueDatePicker;
    @FXML private Label singlePaymentExemptionsLabel;
    @FXML private Button singlePaymentExemptionsButton;
    @FXML private Label singlePaymentCurrencyLabel;
    @FXML private ChoiceBox<String> singlePaymentCurrencyChoiceBox;

    // ===========================
// --- RECURRING PAYMENT FIELDS ---
// ===========================
    @FXML private VBox recurringPaymentsGridPane;
    @FXML private Label recurringPaymentAmountLabel;
    @FXML public Spinner<Integer> recurringPaymentSpinner;
    @FXML private Label recurringPaymentExemptionsLabel;
    @FXML private Button recurringPaymentExemptionsButton;
    @FXML private Label recurringPaymentCurrencyLabel;
    @FXML private ChoiceBox<String> recurringPaymentCurrencyChoiceBox;

    // ===========================
// --- RECURRING PAYMENT OPTIONS (YEARS) ---
// ===========================
    @FXML private Label yearsLabel;
    @FXML private ToggleGroup yearsToggleGroup;
    @FXML private RadioButton setYears;
    @FXML private RadioButton allYears;
    @FXML private Spinner<Integer> yearSpinner;

    // ===========================
// --- MULTI-INSTALLMENT FIELDS ---
// ===========================
    @FXML private VBox multiInstallmentPayments;
    @FXML private Button addRow;
    @FXML private Button removeRow;
    @FXML private ScrollPane multipleInstallmentPaymentsGridPane;
    @FXML private GridPane multipleInstallmentsGrid;

    // ===========================
// --- MONTH CHECKBOXES ---
// ===========================
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

    // ===========================
// --- UTILITY & RESOURCES ---
// ===========================
    @FXML private ResourceBundle resources;

    private Map<Month, CheckBox> monthCheckBoxMap = new HashMap<>();
    private final Map<Integer, ObservableList<MemberExemptionDetails>> installmentExemptionsMap = new HashMap<>();

    private LocalDate groupStartDate;
    private LocalDate groupEndDate;

    public RequiredPaymentController(StyleManager styleManager,
                                     ControllerUtilities controllerUtilities,
                                     SceneEngine sceneEngine,
                                     TabManager tabManager,
                                     AppContext appContext, MemberService memberService, GroupService groupService, CurrencyService currencyService, RequiredPaymentService requiredPaymentService) {
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
        this.sceneEngine = sceneEngine;
        this.tabManager = tabManager;
        this.appContext = appContext;
        this.memberService = memberService;
        this.groupService = groupService;
        this.currencyService = currencyService;
        this.requiredPaymentService = requiredPaymentService;
    }

    @FXML
    public void initialize() {
        log.info("Initializing RequiredPaymentController");

        controllerUtilities.initializeSceneStyle(nameTextField, this);

        // Wire up the radio buttons
        setupPaymentTypeToggles();
        setupYearsToggle();

        monthCheckBoxMap = Map.ofEntries(
                entry(Month.JANUARY, january),
                entry(Month.FEBRUARY, february),
                entry(Month.MARCH, march),
                entry(Month.APRIL, april),
                entry(Month.MAY, may),
                entry(Month.JUNE, june),
                entry(Month.JULY, july),
                entry(Month.AUGUST, august),
                entry(Month.SEPTEMBER, september),
                entry(Month.OCTOBER, october),
                entry(Month.NOVEMBER, november),
                entry(Month.DECEMBER, december)
        );

        groupStartDate = groupService.getGroupStartDate(appContext.getGroupContext().getGroupId());
        groupEndDate = groupService.getGroupEndDate(appContext.getGroupContext().getGroupId());

        configureDatePicker(singlePaymentDueDatePicker, groupStartDate, groupEndDate);

        // Populate currency choice boxes
        List<String> currencies = currencyService.getCurrencyCodeList();
        singlePaymentCurrencyChoiceBox.getItems().addAll(currencies);
        recurringPaymentCurrencyChoiceBox.getItems().addAll(currencies);

        // Name field
        nameTextField.textProperty().addListener(
                (obs, oldVal, newVal) -> updateSaveButtonState());

// Payment type toggle
        paymentType.selectedToggleProperty().addListener(
                (obs, oldVal, newVal) -> updateSaveButtonState());

// Recurring months checkboxes
        monthCheckBoxMap.values().forEach(cb -> cb.selectedProperty().addListener(
                (obs, oldVal, newVal) -> updateSaveButtonState()));

        updateSaveButtonState();
    }

    private void updateSaveButtonState() {
        boolean valid = true;

        // Name is always required
        valid &= !nameTextField.getText().trim().isEmpty();

        // Check payment type specifics
        if (paymentType.getSelectedToggle() == singlePaymentRadioButton) {
            valid &= singlePaymentSpinner.getValue() > 0;
            valid &= singlePaymentCurrencyChoiceBox.getValue() != null;
            valid &= singlePaymentDueDatePicker.getValue() != null;
        } else if (paymentType.getSelectedToggle() == recurringPaymentsRadioButton) {
            valid &= recurringPaymentSpinner.getValue() > 0;
            valid &= recurringPaymentCurrencyChoiceBox.getValue() != null;
            // Optionally: at least one month selected
            valid &= monthCheckBoxMap.values().stream().anyMatch(CheckBox::isSelected);
        } else if (paymentType.getSelectedToggle() == multiInstallmentPaymentsRadioButton) {
            // Ensure each row has all required inputs
            for (int row = 0; row < multipleInstallmentsGrid.getRowCount(); row++) {
                Spinner<Integer> amount = null;
                ChoiceBox<?> currency = null;
                DatePicker date = null;

                for (Node node : multipleInstallmentsGrid.getChildren()) {
                    Integer r = GridPane.getRowIndex(node);
                    Integer c = GridPane.getColumnIndex(node);
                    if (r == null || c == null || r != row) continue;

                    switch (c) {
                        case 0 -> { if (node instanceof Spinner<?> s) amount = (Spinner<Integer>) s; }
                        case 1 -> { if (node instanceof ChoiceBox<?> cb) currency = cb; }
                        case 2 -> { if (node instanceof DatePicker dp) date = dp; }
                    }
                }

                valid &= amount != null && amount.getValue() > 0;
                valid &= currency != null && currency.getValue() != null;
                valid &= date != null && date.getValue() != null;
            }
        }

        saveButton.setDisable(!valid);
        log.debug("Save button enabled: {}", valid);

    }


    private void setupYearsToggle() {
        // Initialize the ToggleGroup
        yearsToggleGroup = new ToggleGroup();
        setYears.setToggleGroup(yearsToggleGroup);
        allYears.setToggleGroup(yearsToggleGroup);

        // Calculate the number of years in the group
        int totalYears = groupEndDate.getYear() - groupStartDate.getYear() + 1;

        // Configure spinner
        SpinnerValueFactory<Integer> yearFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, totalYears, 1);
        yearSpinner.setValueFactory(yearFactory);

        // Default selection
        setYears.setSelected(true);
        yearSpinner.setDisable(false); // spinner enabled by default when "Set Years" is selected

        // Add listener to toggle group
        yearsToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == setYears) {
                yearSpinner.setDisable(false);  // enable spinner
            } else if (newToggle == allYears) {
                yearSpinner.setDisable(true);   // disable spinner
            }
        });
    }


    private void configureDatePicker(DatePicker datePicker ,LocalDate minDate, LocalDate maxDate) {
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                // Disable dates outside the allowed range
                if (empty || date.isBefore(minDate) || date.isAfter(maxDate)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #EEEEEE;"); // Grey out disabled dates
                }
            }
        });

        // Optionally, set default value to minDate
        datePicker.setValue(minDate);
    }


    private void togglePaymentSegment(Toggle selectedToggle) {
        singlePaymentHBox.setVisible(selectedToggle == singlePaymentRadioButton);
        recurringPaymentsGridPane.setVisible(selectedToggle == recurringPaymentsRadioButton);
        multiInstallmentPayments.setVisible(selectedToggle == multiInstallmentPaymentsRadioButton);
    }

    private void setupPaymentTypeToggles() {
        paymentType = new ToggleGroup();
        singlePaymentRadioButton.setToggleGroup(paymentType);
        recurringPaymentsRadioButton.setToggleGroup(paymentType);
        multiInstallmentPaymentsRadioButton.setToggleGroup(paymentType);

        singlePaymentRadioButton.setSelected(true); // default

        // Show the default one
        togglePaymentSegment(paymentType.getSelectedToggle());

        // Show/hide sections based on selected payment type
        paymentType.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == singlePaymentRadioButton) {
                setVisibilityAndManaged(true, false, false);

            } else if (newToggle == recurringPaymentsRadioButton) {
                setVisibilityAndManaged(false, true, false);

            } else if (newToggle == multiInstallmentPaymentsRadioButton) {
                setVisibilityAndManaged(false, false, true);
            }
        });

        // Trigger initial toggle state
        Toggle selected = paymentType.getSelectedToggle();
        if (selected != null) {
            paymentType.getSelectedToggle().getToggleGroup().selectToggle(selected);
        }
    }

    private void setVisibilityAndManaged(boolean singlePayment, boolean recurringPayment, boolean multiInstallmentPayment) {
        singlePaymentHBox.setVisible(singlePayment);
        singlePaymentHBox.setManaged(singlePayment);

        recurringPaymentsGridPane.setVisible(recurringPayment);
        recurringPaymentsGridPane.setManaged(recurringPayment);

        multiInstallmentPayments.setVisible(multiInstallmentPayment);
        multiInstallmentPayments.setManaged(multiInstallmentPayment);
    }

    @FXML
    public void onAddRowClicked(MouseEvent mouseEvent) {
        int rowCount = multipleInstallmentsGrid.getRowCount();

        // --- Numeric spinner for amount ---
        Spinner<Integer> amountSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1_000_000, 0); // min, max, initial
        amountSpinner.setValueFactory(valueFactory);

        // --- Currency choice box ---
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        List<String> currencies = currencyService.getCurrencyCodeList();
        choiceBox.getItems().addAll(currencies);

        // --- Date picker with limits ---
        DatePicker datePicker = new DatePicker();
        configureDatePicker(datePicker, groupStartDate, groupEndDate);

        // --- Exemptions button ---
        Button exemptionsButton = new Button(resources.getString("exemptions"));
        exemptionsButton.setOnMouseClicked(this::onExemptionsButtonClicked);

        // --- Add the new row ---
        multipleInstallmentsGrid.addRow(rowCount,
                amountSpinner, choiceBox, datePicker, exemptionsButton);

        // Multi-installment dynamic rows: call updateSaveButtonState() after adding/removing rows
        updateSaveButtonState();
    }


    @FXML
    public void onRemoveRowClicked(MouseEvent mouseEvent) {
        int rowCount = multipleInstallmentsGrid.getRowCount();
        if (rowCount > 1) { // Ensure at least one row remains
            multipleInstallmentsGrid.getChildren().
                    removeIf(node -> GridPane.getRowIndex(node) == rowCount - 1);
        }
    }

    @FXML
    public void onExemptionsButtonClicked(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = sceneEngine.createSceneComponent(SceneType.MEMBER_EXEMPTION);
            Parent root = loader.load();
            MemberExemptionsController memberExemptionsController = loader.getController();

            // Determine the row for multi-installment (if applicable)
            Node clickedNode = (Node) mouseEvent.getSource();
            Integer rowIndex = GridPane.getRowIndex(clickedNode); // may be null if single/recurring
            if (rowIndex == null) rowIndex = 0; // default for single/recurring



            // Always start with a fresh list of all members for this installment
            ObservableList<MemberExemptionDetails> currentExemptions = FXCollections.observableArrayList();
            for (MemberExemptionDetails member : memberService.getMemberList()) {
                MemberExemptionDetails copy = new MemberExemptionDetails();
                copy.setId(member.getId());
                copy.setName(member.getName());
                copy.setExempted(false); // default, can be changed in popup

                currentExemptions.add(copy);
            }

// If we already have a list in the map, copy over the previous exemption states
            ObservableList<MemberExemptionDetails> previous = installmentExemptionsMap.get(rowIndex);
            if (previous != null) {
                for (MemberExemptionDetails prev : previous) {
                    currentExemptions.stream()
                            .filter(c -> c.getId().equals(prev.getId()))
                            .findFirst()
                            .ifPresent(c -> c.setExempted(prev.isExempted()));
                }
            }

            // Initialize the popup checkboxes
            memberExemptionsController.populateExemptionsList(currentExemptions);

            Popup popup = new Popup();
            popup.getContent().add(root);
            popup.setAutoHide(true);

            Window ownerWindow = clickedNode.getScene().getWindow();
            double x = clickedNode.localToScreen(0, 0).getX();
            double y = clickedNode.localToScreen(0, 0).getY() + clickedNode.getBoundsInParent().getHeight();
            popup.show(ownerWindow, x, y);

            Integer finalRowIndex = rowIndex;
            popup.setOnHidden(event -> {
                // Save updated exemptions for this row
                installmentExemptionsMap.put(finalRowIndex, currentExemptions);
            });


        } catch (Exception e) {
            log.error("Error showing popup", e);
        }
    }

    @FXML
    public void onSaveButtonClicked(MouseEvent mouseEvent) {
        String name = nameTextField.getText();
        List<RequiredPaymentCommand> requiredPaymentCommandList = new ArrayList<>();

        if (paymentType.getSelectedToggle() == singlePaymentRadioButton) {
            requiredPaymentCommandList.add(
                    createCommand(name,
                            singlePaymentSpinner.getValue().doubleValue(),
                            singlePaymentCurrencyChoiceBox.getValue(),
                            singlePaymentDueDatePicker.getValue(),
                            installmentExemptionsMap.get(0))
            );
        } else if (paymentType.getSelectedToggle() == recurringPaymentsRadioButton) {
            requiredPaymentCommandList.addAll(
                    createRecurringCommands(
                            name,
                            recurringPaymentSpinner.getValue().doubleValue(),
                            recurringPaymentCurrencyChoiceBox.getValue(),
                            installmentExemptionsMap.get(0)
                    )
            );
        } else if (paymentType.getSelectedToggle() == multiInstallmentPaymentsRadioButton) {
            int rowCount = multipleInstallmentsGrid.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                RequiredPaymentCommand command = commandFromRow(i);
                if (command != null) requiredPaymentCommandList.add(command);
            }
        }

        // Send to service
        requiredPaymentService.saveRequiredPayments(requiredPaymentCommandList);

    }

    private RequiredPaymentCommand createCommand(String name, double amount, Object currency, LocalDate dueDate, List<MemberExemptionDetails> exemptions) {
        RequiredPaymentCommand command = new RequiredPaymentCommand();
        command.setName(name);
        command.setAmount(amount);
        command.setCurrency(currency != null ? currency.toString() : null);
        command.setDueDate(dueDate);
        if (exemptions != null) {
            command.getMemberExemptionDetails().addAll(exemptions);
        }
        return command;
    }


    private RequiredPaymentCommand commandFromRow(int rowIndex) {
        Spinner<Integer> amountField = null;
        ChoiceBox<?> currencyChoiceBox = null;
        DatePicker dueDatePicker = null;

        for (Node node : multipleInstallmentsGrid.getChildren()) {
            Integer nodeRow = GridPane.getRowIndex(node);
            Integer colIndex = GridPane.getColumnIndex(node);
            if (nodeRow == null || !nodeRow.equals(rowIndex) || colIndex == null) continue;

            switch (colIndex) {
                case 0 -> { if (node instanceof Spinner<?> spinner) amountField = (Spinner<Integer>) spinner; }
                case 1 -> { if (node instanceof ChoiceBox<?> cb) currencyChoiceBox = cb; }
                case 2 -> { if (node instanceof DatePicker dp) dueDatePicker = dp; }
            }
        }

        if (amountField != null && currencyChoiceBox != null && dueDatePicker != null) {
            return createCommand(
                    nameTextField.getText(),
                    amountField.getValue().doubleValue(),
                    currencyChoiceBox.getValue(),
                    dueDatePicker.getValue(),
                    installmentExemptionsMap.get(rowIndex)
            );
        }
        return null;
    }

    private List<RequiredPaymentCommand> createRecurringCommands(String name, double amount, Object currency, List<MemberExemptionDetails> exemptions) {
        int startYear = groupStartDate.getYear();
        int endYear = yearsToggleGroup.getSelectedToggle() == setYears
                ? startYear + yearSpinner.getValue() - 1
                : groupEndDate.getYear();

        List<RequiredPaymentCommand> commands = new ArrayList<>();
        for (int year = startYear; year <= endYear; year++) {
            for (Map.Entry<Month, CheckBox> entry : monthCheckBoxMap.entrySet()) {
                Month month = entry.getKey();
                CheckBox box = entry.getValue();
                if (!box.isSelected()) continue;

                LocalDate dueDate = LocalDate.of(year, month, 1);
                // Skip if out of group range
                if (dueDate.isBefore(groupStartDate) || dueDate.isAfter(groupEndDate)) continue;

                commands.add(createCommand(name, amount, currency, dueDate, exemptions));
            }
        }
        return commands;
    }

    @FXML
    public void onCancelButtonClicked(MouseEvent mouseEvent) {
        controllerUtilities.closeStage(nameTextField);
    }

    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(nameTextField.getScene(), this);
    }
}
