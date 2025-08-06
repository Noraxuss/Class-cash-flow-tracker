package cash_flow.controller;

import cash_flow.context.AppContext;
import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.controller.utilities.TabManager;
import cash_flow.controller.utilities.tablecell.DatePickerTableCell;
import cash_flow.controller.utilities.tablecell.EditableTextCell;
import cash_flow.controller.utilities.tablecell.TableManager;
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
import javafx.util.converter.DefaultStringConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

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
    private final TableManager tableManager;

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
     * @param styleManager          The StyleManager to manage styles.
     * @param controllerUtilities   Utility methods for controller operations.
     * @param appContext            The application context providing group context.
     * @param guardianService       Service for managing guardians.
     * @param memberService         Service for managing members.
     * @param inUIValidationService Service for in-UI validation.
     */
    public AddGroupMembersController(StyleManager styleManager,
                                     ControllerUtilities controllerUtilities,
                                     AppContext appContext,
                                     GuardianService guardianService,
                                     MemberService memberService,
                                     InUIValidationService inUIValidationService,
                                     LoadingController loadingController,
                                     SceneEngine sceneEngine,
                                     TableManager tableManager) {
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
        this.appContext = appContext;
        this.guardianService = guardianService;
        this.memberService = memberService;
        this.inUIValidationService = inUIValidationService;
        this.loadingController = loadingController;
        this.sceneEngine = sceneEngine;
        this.tableManager = tableManager;
    }

    public void initialize() {
        log.info("AddGroupMembersController initialized");

        controllerUtilities.initializeSceneStyle(membersTableView, this);

        membersTableView.setEditable(true);
        // Setup table as editable and configure columns via TableManager using Enum
        tableManager.configureGroupMemberTable(
                membersTableView,
                this::createDefaultGroupMember
        );

        groupMembers = FXCollections.observableArrayList();
        membersTableView.setItems(groupMembers);

        membersTableView.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Platform.runLater(() -> {
                    GroupMemberRowModel newMember = tableManager.newEmptyGroupMemberRowWithDefaults();
                    log.info(newMember.toString());
                    groupMembers.add(newMember);
                    membersTableView.getSelectionModel().select(0);
                    membersTableView.edit(0, membersTableView.getColumns().getFirst());
                });
            }
        });

        // Keyboard navigation (Tab / Enter to go to next editable cell)
        membersTableView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) {
                event.consume(); // prevent default behavior
                tableManager.moveToNextEditableCell(
                        membersTableView,
                        optionalColumn, // Column to ignore (if needed)
                        groupMembers,
                        this::createDefaultGroupMember
                );
            }
        });
    }

    private GroupMemberRowModel createDefaultGroupMember() {
        return tableManager.newEmptyGroupMemberRowWithDefaults();
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
