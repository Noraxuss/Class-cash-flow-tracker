package cash_flow.dto;

import javafx.beans.property.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents a row in the group member table, containing both member and guardian information.
 * This model is used to display and edit group member details in a table format.
 */
@NoArgsConstructor
@Getter
public class GroupMemberRowModel {

    // Member info
    private final StringProperty memberFirstName = new SimpleStringProperty();
    private final StringProperty memberLastName = new SimpleStringProperty();
    private final StringProperty memberEmail = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> memberJoinDate = new SimpleObjectProperty<>();

    // Guardian info
    private final StringProperty guardianFirstName = new SimpleStringProperty();
    private final StringProperty guardianLastName = new SimpleStringProperty();
    private final StringProperty guardianEmail = new SimpleStringProperty();

    // === Property Getters ===

    public StringProperty memberFirstNameProperty() {
        return memberFirstName;
    }

    public StringProperty memberLastNameProperty() {
        return memberLastName;
    }

    public StringProperty memberEmailProperty() {
        return memberEmail;
    }

    public ObjectProperty<LocalDate> memberJoinDateProperty() {
        return memberJoinDate;
    }

    public StringProperty guardianFirstNameProperty() {
        return guardianFirstName;
    }

    public StringProperty guardianLastNameProperty() {
        return guardianLastName;
    }

    public StringProperty guardianEmailProperty() {
        return guardianEmail;
    }

    // === Setters ===

    public void setMemberFirstName(String memberFirstName) {
        this.memberFirstName.set(memberFirstName);
    }

    public void setMemberLastName(String memberLastName) {
        this.memberLastName.set(memberLastName);
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail.set(memberEmail);
    }

    public void setMemberJoinDate(LocalDate joinDate) {
        this.memberJoinDate.set(joinDate);
    }

    public void setGuardianFirstName(String guardianFirstName) {
        this.guardianFirstName.set(guardianFirstName);
    }

    public void setGuardianLastName(String guardianLastName) {
        this.guardianLastName.set(guardianLastName);
    }

    public void setGuardianEmail(String guardianEmail) {
        this.guardianEmail.set(guardianEmail);
    }
}
