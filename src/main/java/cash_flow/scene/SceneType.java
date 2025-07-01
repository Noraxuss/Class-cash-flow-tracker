package cash_flow.scene;

import lombok.Getter;

@Getter
public enum SceneType {

    BASE("/scenes/base/base_scene.properties"),
    LOADING("/scenes/loading/loading_scene.properties"),
    ADD_GROUP_OVERSEER("/scenes/add_group_overseer/add_group_overseer_scene.properties"),
    ADD_GROUP_MEMBERS("/scenes/add_group_members/add_group_members_scene.properties"),
    CREATE_GROUP("/scenes/create_group/create_group_scene.properties"),
    LIST_CELL("/scenes/list_cell/list_cell_scene.properties"),
    GROUP_CHOOSING("/scenes/group_choosing/choose_cash_collection_group_scene.properties");

    private final String propertiesFilePath;

    SceneType(String propertiesFilePath) {
        this.propertiesFilePath = propertiesFilePath;
    }
}
