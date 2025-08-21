package cash_flow.domain;

import lombok.Getter;

@Getter
public enum GroupEventType {
    GROUP_CREATED,
    GROUP_ENDED,
    GROUP_UPDATED,
    MEMBER_ADDED_TO_GROUP,
    MEMBER_REMOVED_FROM_GROUP,
    MEMBER_UPDATED
}
