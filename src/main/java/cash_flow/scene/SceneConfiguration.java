package cash_flow.scene;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Getter
@Setter
@Slf4j
@Component
public class SceneConfiguration {

    private String id;
    private String fxml;
    private String cssLight;
    private String cssDark;
    private String controller;
    private String title;
    private int width;
    private int height;
    private boolean resizable;
    private String transition;
    private String placement;
    private String messages;

    public void fillSceneConfig(Properties props) {
        log.info("Filling scene config");

        this.id = props.getProperty(ScenePropertyKey.SCENE_ID.getPropertyName());
        this.fxml = props.getProperty(ScenePropertyKey.SCENE_FXML.getPropertyName());
        this.cssLight = props.getProperty(ScenePropertyKey.SCENE_CSS_LIGHT.getPropertyName());
        this.cssDark = props.getProperty(ScenePropertyKey.SCENE_CSS_DARK.getPropertyName());
        this.controller = props.getProperty(ScenePropertyKey.SCENE_CONTROLLER.getPropertyName());
        this.title = props.getProperty(ScenePropertyKey.SCENE_TITLE.getPropertyName());
        this.width = Integer.parseInt(props.getProperty(ScenePropertyKey.SCENE_SIZE_WIDTH.getPropertyName()));
        this.height = Integer.parseInt(props.getProperty(ScenePropertyKey.SCENE_SIZE_HEIGHT.getPropertyName()));
        this.resizable = Boolean.parseBoolean(props.getProperty(ScenePropertyKey.SCENE_RESIZABLE.getPropertyName()));
        this.transition = props.getProperty(ScenePropertyKey.SCENE_TRANSITION.getPropertyName());
        this.placement = props.getProperty(ScenePropertyKey.SCENE_PLACEMENT.getPropertyName());
        this.messages = props.getProperty(ScenePropertyKey.SCENE_MESSAGES.getPropertyName());
    }

    @Override
    public String toString() {
        return "SceneConfiguration{" +
                "id='" + id + '\'' +
                ", fxml='" + fxml + '\'' +
                ", cssLight='" + cssLight + '\'' +
                ", cssDark='" + cssDark + '\'' +
                ", controller='" + controller + '\'' +
                ", title='" + title + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", resizable=" + resizable +
                ", transition='" + transition + '\'' +
                ", placement='" + placement + '\'' +
                ", messages='" + messages + '\'' +
                '}';
    }
}
