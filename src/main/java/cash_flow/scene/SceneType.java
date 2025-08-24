package cash_flow.scene;

import lombok.Getter;

@Getter
public enum SceneType {

    BASE("/scenes/base/base_scene.properties"),
    LOADING("/scenes/loading/loading_scene.properties"),
    ADD_GROUP_OVERSEER("/scenes/add_group_overseer/add_group_overseer_scene.properties"),
    ADD_GROUP_MEMBERS("/scenes/add_group_members/add_members_scene.properties"),
    CREATE_GROUP("/scenes/create_group/create_group_scene.properties"),
    LIST_CELL("/scenes/list_cell/list_cell_scene.properties"),
    GROUP_CHOOSING("/scenes/group_choosing/choose_cash_collection_group_scene.properties"),
    ADD_PAYMENT("/scenes/add_payment/add_payment_scene.properties"),
    SPLIT_CENTER("/scenes/split_center/split_center_scene.properties"),
    GROUP_OVERVIEW("/scenes/group_overview/group_overview_scene.properties"),
    MEMBER_OVERVIEW("/scenes/member_overview/member_overview_scene.properties"),
    PAYMENT("/scenes/payment/payment_scene.properties"),
    MEMBER_EXEMPTION("/scenes/member_exemptions/member_exemptions_scene.properties"),
    USER_LOGS("/scenes/user_logs/user_logs_scene.properties"),
    REQUIRED_PAYMENT("/scenes/required_payment/required_payment_scene.properties");


//    GROUP_DATA("/not-in-use/group_data-not_in_use/group_data_scene.properties"),
//    MEMBER_DATA("/not-in-use/member_data-not_in_use/member_data_scene.properties"),
//    GROUP_OVERVIEW("/not-in-use/group_overview-not_in_use/group_overview_scene.properties"),
//    GROUP_MEMBER_DATA("/not-in-use/group_member_data-not_in_use/group_member_data_scene.properties");

    private final String propertiesFilePath;

    SceneType(String propertiesFilePath) {
        this.propertiesFilePath = propertiesFilePath;
    }
}
