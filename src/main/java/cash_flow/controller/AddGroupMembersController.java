package cash_flow.controller;

import cash_flow.context.AppContext;
import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.controller.utilities.tablecell.TableManager;
import cash_flow.dto.GroupMemberRowModel;
import cash_flow.dto.incoming.GroupMemberCreationCommand;
import cash_flow.dto.incoming.GuardianCreationCommand;
import cash_flow.scene.SceneEngine;
import cash_flow.service.GuardianService;
import cash_flow.service.InUIValidationService;
import cash_flow.service.MemberService;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

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
                    log.info("Adding new member row: {}", newMember);
                    groupMembers.add(newMember);
                    membersTableView.getSelectionModel().select(0);

                    // DELAY EDIT UNTIL LAYOUT IS GUARANTEED
                    PauseTransition pause = new PauseTransition(Duration.millis(80));
                    pause.setOnFinished(e -> {
                        TableColumn<GroupMemberRowModel, ?> firstCol = membersTableView.getVisibleLeafColumn(0);
                        log.info("Starting manual edit: row 0, column {}", firstCol.getText());
                        membersTableView.edit(0, firstCol);
                    });
                    pause.play();
                });
            }
        });

        membersTableView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                event.consume(); // Prevent default

                // Delay move to next editable cell slightly
                PauseTransition pause = new PauseTransition(Duration.millis(40));
                pause.setOnFinished(e -> {
                    tableManager.moveToNextEditableCell(
                            membersTableView,
                            optionalColumn,
                            groupMembers,
                            this::createDefaultGroupMember
                    );
                });
                pause.play();
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
            // Validate required fields
            if (checkIfGroupMemberExists(groupMember)) continue; // Skip incomplete members
            loadingController.updateProgressLabel(progress + " / " + groupMembers.size());
            String guardianId = null;
            try {
                StringProperty firstNameProp = groupMember.getGuardianFirstName();
                StringProperty lastNameProp = groupMember.getGuardianLastName();
                StringProperty emailProp = groupMember.getGuardianEmail();

                if (checkIfGuardianExists(firstNameProp, lastNameProp, emailProp)) {
                    // Create guardian if all fields are present
                    GuardianCreationCommand guardianCreationCommand = new GuardianCreationCommand(
                            firstNameProp.get(),
                            lastNameProp.get(),
                            emailProp.get());
                    // Check if the guardian already exists
                    guardianId = guardianService.createGuardian(guardianCreationCommand);
                    log.info("Created or found guardian: {} {}, ID: {}",
                            firstNameProp.get(),
                            lastNameProp.get(),
                            guardianId);
                }

                if (!checkIfGroupMemberExists(groupMember)) {
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
                }

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

    private static boolean checkIfGuardianExists(StringProperty guardianFirstName, StringProperty guardianLastName, StringProperty guardianEmail) {
        return guardianFirstName != null &&
                guardianLastName != null &&
                guardianEmail != null &&
                guardianFirstName.get() != null &&
                !guardianFirstName.get().isBlank() &&
                guardianLastName.get() != null &&
                !guardianLastName.get().isBlank() &&
                guardianEmail.get() != null &&
                !guardianEmail.get().isBlank();
    }

    private static boolean checkIfGroupMemberExists(GroupMemberRowModel groupMember) {
        try {
            if (groupMember.getMemberFirstName() == null ||
                    groupMember.getMemberFirstName().get().isBlank() ||
                    groupMember.getMemberLastName() == null ||
                    groupMember.getMemberLastName().get().isBlank() ||
                    groupMember.getMemberEmail() == null ||
                    groupMember.getMemberEmail().get().isBlank()) {
                log.warn("Skipping incomplete member: {} {}, missing required fields.",
                        groupMember.getMemberFirstName().orElse("UNKNOWN"),
                        groupMember.getMemberLastName().orElse("UNKNOWN"));
                return true;
            }
            return false;
        } catch (Exception e) {
            log.info("I am Your issue: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void backButtonClicked(ActionEvent actionEvent) {
        controllerUtilities.closeStage(backButton);
    }
}
