package cash_flow.domain;

import lombok.Getter;

@Getter
public enum PersonType {

    GROUP_MEMBER("GM"),
    GUARDIAN("GU"),
    OVERSEER("OV");

    private final String typeName;

    PersonType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
