package cash_flow.scene;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class SceneConfigurationLoader {

    private final SceneConfiguration sceneConfiguration;

    public SceneConfigurationLoader(SceneConfiguration sceneConfiguration) {
        this.sceneConfiguration = sceneConfiguration;
    }

    public SceneConfiguration load(SceneType type) {
        Properties props = new Properties();
        try (InputStream input = getClass().getResourceAsStream(type.getPropertiesFilePath())) {
            props.load(input);
            sceneConfiguration.fillSceneConfig(props);
            return sceneConfiguration;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load scene properties", e);
        }
    }
}
