package cash_flow.controller;

import cash_flow.context.AppContext;
import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.controller.utilities.tablecell.DatePickerTableCell;
import cash_flow.controller.utilities.tablecell.EditableTextCell;
import cash_flow.dto.GroupMemberRowModel;
import cash_flow.dto.GroupMemberRowModelEnum;
import cash_flow.dto.incoming.GroupMemberCreationCommand;
import cash_flow.dto.incoming.GuardianCreationCommand;
import cash_flow.scene.SceneEngine;
import cash_flow.service.GuardianService;
import cash_flow.service.InUIValidationService;
import cash_flow.service.MemberService;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

@Component
@Slf4j
public class AddGroupMembersController implements ThemeChangeListener {

    private final StyleManager styleManager;
    private final ControllerUtilities controllerUtilities;
    private final AppContext appContext;
    private final GuardianService guardianService;
    private final MemberService memberService;
    private final InUIValidationService inUIValidationService;
    private final LoadingController loadingController;
    private final SceneEngine sceneEngine;

    @FXML
    private TableView<GroupMemberRowModel> membersTableView;

    @FXML
    private TableColumn<GroupMemberRowModel, String> firstNameColumn;

    @FXML
    private TableColumn<GroupMemberRowModel, String> lastNameColumn;

    @FXML
    private TableColumn<GroupMemberRowModel, String> emailColumn;

    @FXML
    private TableColumn<GroupMemberRowModel, LocalDate> joinDateColumn;

    @FXML
    private TableColumn<GroupMemberRowModel, ?> optionalColumn;

    @FXML
    private TableColumn<GroupMemberRowModel, String> guardianFirstNameColumn;

    @FXML
    private TableColumn<GroupMemberRowModel, String> guardianLastNameColumn;

    @FXML
    private TableColumn<GroupMemberRowModel, String> guardianEmailColumn;

    @FXML
    public HBox buttonsHBox;

    @FXML
    public Button saveChangesButton;

    @FXML
    public Button backButton;

    private ObservableList<GroupMemberRowModel> groupMembers;

    /**
     * Constructor for AddGroupMembersController.
     *
     * @param styleManager            The StyleManager to manage styles.
     * @param controllerUtilities     Utility methods for controller operations.
     * @param appContext              The application context providing group context.
     * @param guardianService         Service for managing guardians.
     * @param memberService           Service for managing members.
     * @param inUIValidationService   Service for in-UI validation.
     */
    public AddGroupMembersController(StyleManager styleManager, ControllerUtilities controllerUtilities, AppContext appContext, GuardianService guardianService, MemberService memberService, InUIValidationService inUIValidationService, LoadingController loadingController, SceneEngine sceneEngine) {
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
        this.appContext = appContext;
        this.guardianService = guardianService;
        this.memberService = memberService;
        this.inUIValidationService = inUIValidationService;
        this.loadingController = loadingController;
        this.sceneEngine = sceneEngine;
    }

    public void initialize() {
        log.info("AddGroupMembersController initialized");
        // Additional initialization logic can be added here if needed
        controllerUtilities.initializeSceneStyle(membersTableView, this);

        configureTable();

        // Set up the table to be editable
        groupMembers = FXCollections.observableArrayList();
        membersTableView.sceneProperty()
                .addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Platform.runLater(() -> {
                    GroupMemberRowModel newMember = new GroupMemberRowModel();
                    newMember.setMemberJoinDate(appContext.getGroupContext().getStartDate());
                    groupMembers.add(newMember);
                    membersTableView.setItems(groupMembers);
                    membersTableView.getSelectionModel().select(0);
                    membersTableView.edit(0, membersTableView.getColumns().getFirst());
                });
            }
        });

        // Set up the columns to be editable
        firstNameColumn.setOnEditCommit(event ->
                handleEditCommit(event, GroupMemberRowModel::setMemberFirstName));
        lastNameColumn.setOnEditCommit(event ->
                handleEditCommit(event, GroupMemberRowModel::setMemberLastName));
        emailColumn.setOnEditCommit(event ->
                handleEditCommit(event, GroupMemberRowModel::setMemberEmail));

        // Set up the join date column to be editable with a DatePicker
        membersTableView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) {
                event.consume(); // prevent default tabbing
                moveToNextCell();
            }
        });
    }

    private void configureTable() {
        membersTableView.setEditable(true);

        firstNameColumn.setUserData(GroupMemberRowModelEnum.MEMBER_FIRST_NAME);
        lastNameColumn.setUserData(GroupMemberRowModelEnum.MEMBER_LAST_NAME);
        emailColumn.setUserData(GroupMemberRowModelEnum.MEMBER_EMAIL);
        joinDateColumn.setUserData(GroupMemberRowModelEnum.JOIN_DATE);
        guardianFirstNameColumn.setUserData(GroupMemberRowModelEnum.GUARDIAN_FIRST_NAME);
        guardianLastNameColumn.setUserData(GroupMemberRowModelEnum.GUARDIAN_LAST_NAME);
        guardianEmailColumn.setUserData(GroupMemberRowModelEnum.GUARDIAN_EMAIL);

        for (TableColumn<GroupMemberRowModel, ?> column : membersTableView.getColumns()) {
            if (column.equals(optionalColumn)) {
                for (TableColumn<GroupMemberRowModel, ?> optionalColumnColumn : optionalColumn.getColumns()) {
                    setupColumn(optionalColumnColumn);
                }
            }
            setupColumn(column);
        }
    }

    @SuppressWarnings("unchecked")
    private void setupColumn(TableColumn<GroupMemberRowModel, ?> rawColumn) {
        // Check if the column has user data set
        GroupMemberRowModelEnum field = (GroupMemberRowModelEnum) rawColumn.getUserData();

        if (field == null) {
            log.warn("Column {} has no user data set, skipping setup", rawColumn.getText());
            return;
        }

        // Set the text of the column
        if (field.getClassType().equals(String.class)) {
            log.info("Setting up String column: {}", rawColumn.getText());
            TableColumn<GroupMemberRowModel, String> column = (TableColumn<GroupMemberRowModel, String>) rawColumn;

            column.setCellValueFactory(new PropertyValueFactory<>(field.getPropertyName()));

            // Use your custom cell factory
            column.setCellFactory(param ->
                            new EditableTextCell<>(new DefaultStringConverter(),
                            inUIValidationService,
                                    (GroupMemberRowModelEnum) rawColumn.getUserData()));

            column.setOnEditCommit(event -> {
                GroupMemberRowModel row = event.getRowValue();
                setValue(row, field, event.getNewValue());
            });
        } else if (field.getClassType().equals(LocalDate.class)) {
            log.info("Setting up LocalDate column: {}", rawColumn.getText());
            setupDatePickerColumn((TableColumn<GroupMemberRowModel, LocalDate>) rawColumn);
        } else {
            log.warn("Unsupported field type: {} for column {}", field.getClassType(), rawColumn.getText());
        }
    }

    private void setupDatePickerColumn(TableColumn<GroupMemberRowModel, LocalDate> dateColumn) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        dateColumn.setCellValueFactory(new PropertyValueFactory<>(GroupMemberRowModelEnum.JOIN_DATE.getPropertyName()));

        dateColumn.setCellFactory(param
                -> new DatePickerTableCell<>(formatter, inUIValidationService));

        dateColumn.setOnEditCommit(event -> {
            GroupMemberRowModel row = event.getRowValue();
            row.setMemberJoinDate(event.getNewValue());
        });
    }

    private void setValue(GroupMemberRowModel row, GroupMemberRowModelEnum field, String newValue) {
        switch (field) {
            case MEMBER_FIRST_NAME -> row.setMemberFirstName(newValue);
            case MEMBER_LAST_NAME -> row.setMemberLastName(newValue);
            case MEMBER_EMAIL -> row.setMemberEmail(newValue);
            case GUARDIAN_FIRST_NAME -> row.setGuardianFirstName(newValue);
            case GUARDIAN_LAST_NAME -> row.setGuardianLastName(newValue);
            case GUARDIAN_EMAIL -> row.setGuardianEmail(newValue);
            default -> log.warn("Unsupported field type: {}", field);
        }
    }

    private void handleEditCommit(TableColumn.CellEditEvent<GroupMemberRowModel, String> event,
                                  BiConsumer<GroupMemberRowModel, String> propertySetter) {
        GroupMemberRowModel editedItem = event.getRowValue();
        String newValue = event.getNewValue();

        propertySetter.accept(editedItem, newValue);

        ObservableList<GroupMemberRowModel> items = membersTableView.getItems();

        boolean isLastRow = items.indexOf(editedItem) == items.size() - 1;

        if (isLastRow && isRowNotEmpty(editedItem)) {
            // Delay the row addition to avoid JavaFX skin conflict
            Platform.runLater(() -> {
                if (!containsEmptyRow(items)) {
                    GroupMemberRowModel member = new GroupMemberRowModel();
                    member.setMemberJoinDate(appContext.getGroupContext().getStartDate());
                    items.add(member);
                }
            });
        }
    }

    private boolean isRowNotEmpty(GroupMemberRowModel row) {
        if (row == null) {
            return false;
        }
        String first = row.getMemberFirstName().get();
        String last = row.getMemberLastName().get();
        String email = row.getMemberEmail().get();
        return (first != null && !first.isEmpty()) ||
                (last != null && !last.isEmpty()) ||
                (email != null && !email.isEmpty());
    }

    private boolean containsEmptyRow(ObservableList<GroupMemberRowModel> items) {
        return items.stream().anyMatch(row -> !isRowNotEmpty(row));
    }

    private void moveToNextCell() {
        TableView.TableViewFocusModel<GroupMemberRowModel> focusModel = membersTableView.getFocusModel();
        TablePosition<GroupMemberRowModel, ?> pos = focusModel.getFocusedCell();

        int currentRow = pos.getRow();
        int currentCol = pos.getColumn();

        List<TableColumn<GroupMemberRowModel, ?>> columns = new ArrayList<>(membersTableView.getColumns());
        columns.removeIf(column -> column.equals(optionalColumn));
        columns.addAll(optionalColumn.getColumns());

        int nextCol = currentCol + 1;
        int nextRow = currentRow;

        if (nextCol >= columns.size()) {
            nextCol = 0;
            nextRow++;
        }

        // If we're beyond the last row, add a new row
        if (nextRow >= groupMembers.size()) {
            groupMembers.add(new GroupMemberRowModel());
        }

        // Scroll and edit the next cell
        final int targetRow = nextRow;
        final int targetCol = nextCol;

        // Ensure the target row and column are valid
        Platform.runLater(() -> {
            membersTableView.scrollTo(targetRow);
            membersTableView.scrollToColumn(columns.get(targetCol));
            membersTableView.getSelectionModel().clearAndSelect(targetRow, columns.get(targetCol));
            membersTableView.getFocusModel().focus(targetRow, columns.get(targetCol));
            membersTableView.edit(targetRow, columns.get(targetCol));
        });
    }


    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(membersTableView.getScene(), this);
    }

    @FXML
    public void saveChangesClicked(ActionEvent actionEvent) {
        loadingController.showProgressBarLoading();
        sceneEngine.showLoadingScene();
        int progress = 1;
        for (GroupMemberRowModel groupMember : groupMembers) {
            loadingController.updateProgressLabel(progress + " / " + groupMembers.size());
            String guardianId = null;
            try {
                StringProperty firstNameProp = groupMember.getGuardianFirstName();
                StringProperty lastNameProp = groupMember.getGuardianLastName();
                StringProperty emailProp = groupMember.getGuardianEmail();

                if (firstNameProp != null && lastNameProp != null && emailProp != null &&
                        firstNameProp.get() != null && !firstNameProp.get().isBlank() &&
                        lastNameProp.get() != null && !lastNameProp.get().isBlank() &&
                        emailProp.get() != null && !emailProp.get().isBlank()) {

                    GuardianCreationCommand guardianCreationCommand = new GuardianCreationCommand(
                            firstNameProp.get(),
                            lastNameProp.get(),
                            emailProp.get());

                    guardianId = guardianService.createGuardian(guardianCreationCommand);
                }


                // Create member command including guardianId (null if no guardian)
                GroupMemberCreationCommand groupMemberCreationCommand = new GroupMemberCreationCommand(
                        groupMember.getMemberFirstName().get(),
                        groupMember.getMemberLastName().get(),
                        groupMember.getMemberEmail().get(),
                        groupMember.getMemberJoinDate().get(),
                        guardianId);

                // Create member
                memberService.createMember(groupMemberCreationCommand);

                log.info("Created member: {} {}, with guardian ID: {}",
                        groupMember.getMemberFirstName().get(),
                        groupMember.getMemberLastName().get(),
                        guardianId);

            } catch (Exception e) {
                log.error("Failed to create member or guardian for: {} {}, error: {}",
                        groupMember.getMemberFirstName().orElse("UNKNOWN"),
                        groupMember.getMemberLastName().orElse("UNKNOWN"),
                        e.getMessage(), e);
                // Decide if you want to continue or break here
            }
            progress++;
        }
        controllerUtilities.closeStage(saveChangesButton);
        loadingController.showSpinnerLoading();
        sceneEngine.hideLoadingScene();
        log.info("All members processed, returning to previous scene.");
    }

    @FXML
    public void backButtonClicked(ActionEvent actionEvent) {
        controllerUtilities.closeStage(backButton);
    }
}
