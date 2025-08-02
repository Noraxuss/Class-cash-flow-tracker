package cash_flow.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public enum GroupMemberRowModelEnum {

    MEMBER_FIRST_NAME("memberFirstName", String.class),
    MEMBER_LAST_NAME("memberLastName", String.class),
    MEMBER_EMAIL("memberEmail", String.class),
    GUARDIAN_FIRST_NAME("guardianFirstName", String.class),
    GUARDIAN_LAST_NAME("guardianLastName", String.class),
    GUARDIAN_EMAIL("guardianEmail", String.class),
    JOIN_DATE("memberJoinDate", LocalDate.class);

    private final String propertyName;
    private final Class<?> classType;


    GroupMemberRowModelEnum(String propertyName, Class<?> classType) {
        this.propertyName = propertyName;

        this.classType = classType;
    }
}
