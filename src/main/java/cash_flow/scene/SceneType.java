package cash_flow.scene;

import lombok.Getter;

@Getter
public enum SceneType {

    BASE("/scenes/base/base_scene.properties"),
    LOADING("/scenes/loading/loading_scene.properties"),
    ADD_GROUP_OVERSEER("/scenes/add_group_overseer/add_group_overseer_scene.properties"),
    ADD_GROUP_MEMBERS("/scenes/add_group_members/add_members_scene.properties"),
    CREATE_GROUP("/scenes/create_group/create_scene.properties"),
    LIST_CELL("/scenes/list_cell/list_cell_scene.properties"),
    GROUP_CHOOSING("/scenes/group_choosing/choose_cash_collection_group_scene.properties"),
    GROUP_DATA("/scenes/group_data/group_data_scene.properties"),
    MEMBER_DATA("/scenes/member_data/member_data_scene.properties"),
    SPLIT_CENTER("/scenes/split_center/split_center_scene.properties"),
    GROUP_OVERVIEW("/scenes/group_overview/group_overview_scene.properties"),
    GROUP_MEMBER_DATA("/scenes/group_member_data/group_member_data_scene.properties");



    private final String propertiesFilePath;

    SceneType(String propertiesFilePath) {
        this.propertiesFilePath = propertiesFilePath;
    }
}
