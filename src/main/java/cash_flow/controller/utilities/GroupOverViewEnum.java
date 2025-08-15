package cash_flow.controller.utilities;

import cash_flow.scene.SceneType;
import lombok.Getter;

@Getter
public enum GroupOverViewEnum {

    OVERVIEW("overview", "group-overview-button", SceneType.GROUP_OVERVIEW),
//    PAYMENTS("payments", "group-payments-button", SceneType.P),
    ;

    private final String id;
    private final String buttonMessagesId;
    private final SceneType sceneType;

    GroupOverViewEnum(String id, String buttonMessagesId, SceneType sceneType) {
        this.id = id;
        this.buttonMessagesId = buttonMessagesId;
        this.sceneType = sceneType;
    }
}
