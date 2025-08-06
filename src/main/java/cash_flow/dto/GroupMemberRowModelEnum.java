package cash_flow.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.util.function.BiConsumer;

@Getter
public enum GroupMemberRowModelEnum implements PropertyName<Object, GroupMemberRowModel> {

    MEMBER_FIRST_NAME("memberFirstName", String.class, EditingType.TEXT, (row, value) -> row.setMemberFirstName((String) value)),
    MEMBER_LAST_NAME("memberLastName", String.class, EditingType.TEXT, (row, value) -> row.setMemberLastName((String) value)),
    MEMBER_EMAIL("memberEmail", String.class, EditingType.TEXT, (row, value) -> row.setMemberEmail((String) value)),
    GUARDIAN_FIRST_NAME("guardianFirstName", String.class, EditingType.TEXT, (row, value) -> row.setGuardianFirstName((String) value)),
    GUARDIAN_LAST_NAME("guardianLastName", String.class, EditingType.TEXT, (row, value) -> row.setGuardianLastName((String) value)),
    GUARDIAN_EMAIL("guardianEmail", String.class, EditingType.TEXT, (row, value) -> row.setGuardianEmail((String) value)),
    JOIN_DATE("memberJoinDate", LocalDate.class, EditingType.DATE_PICKER, (row, value) -> row.setMemberJoinDate((LocalDate) value));

    private final String propertyName;
    private final Class<?> classType;
    private final EditingType editingType;
    private final BiConsumer<GroupMemberRowModel, Object> setter;

    GroupMemberRowModelEnum(String propertyName,
                            Class<?> classType,
                            EditingType editingType,
                            BiConsumer<GroupMemberRowModel, Object> setter) {
        this.propertyName = propertyName;
        this.classType = classType;
        this.editingType = editingType;
        this.setter = setter;
    }

    @Override
    public void setValue(GroupMemberRowModel row, Object value) {
        setter.accept(row, value);
    }
}

