package cash_flow.controller;

import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.dto.incoming.GroupMemberCreationCommand;
import cash_flow.dto.incoming.GuardianCreationCommand;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.application.Platform;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AddGroupMembersController implements ThemeChangeListener {

    public TableView membersTableView;
    public TableColumn firstNameColumn;
    public TableColumn lastNameColumn;
    public TableColumn emailColumn;
    public TableColumn joinDateColumn;
    public TableColumn optionalColumn;
    public TableColumn parentFirstNameColumn;
    public TableColumn parentLastNameColumn;
    public TableColumn parentEmailColumn;

    private final StyleManager styleManager;
    private final ControllerUtilities controllerUtilities;

    public AddGroupMembersController(StyleManager styleManager, ControllerUtilities controllerUtilities) {
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
    }

    public void initialize() {
        log.info("AddGroupMembersController initialized");
        // Additional initialization logic can be added here if needed
        controllerUtilities.initializeSceneStyle(membersTableView, this);

        GuardianCreationCommand guardianCreationCommand = new GuardianCreationCommand(
                parentFirstNameColumn.getText(),
                parentLastNameColumn.getText(),
                parentEmailColumn.getText());

        Long guardianId = null; // TODO get ID from repository

        GroupMemberCreationCommand groupMemberCreationCommand = new GroupMemberCreationCommand(
                firstNameColumn.getText(),
                lastNameColumn.getText(),
                emailColumn.getText(),
                guardianId);
    }


    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(membersTableView.getScene(), this);
    }
}
