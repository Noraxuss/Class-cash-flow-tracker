package cash_flow.scene;

import lombok.Getter;

/**
 * Enum representing the properties used in scene configuration.
 * Each enum constant corresponds to a property name used in the scene configuration.
 */
@Getter
public enum ScenePropertyKey {

    SCENE_ID("scene.id"),
    SCENE_FXML("scene.fxml"),
    SCENE_CSS_LIGHT("scene.css.light"),
    SCENE_CSS_DARK("scene.css.dark"),
    SCENE_CONTROLLER("scene.controller"),
    SCENE_TITLE("scene.title"),
    SCENE_SIZE_WIDTH("scene.size.width"),
    SCENE_SIZE_HEIGHT("scene.size.height"),
    SCENE_RESIZABLE("scene.resizable"),
    SCENE_TRANSITION("scene.transition"),
    SCENE_PLACEMENT("scene.placement"),
    SCENE_MESSAGES("scene.messages"),
    SCENE_SIDE("scene.side");

    private final String propertyName;

    ScenePropertyKey(String propertyName) {
        this.propertyName = propertyName;
    }
}
