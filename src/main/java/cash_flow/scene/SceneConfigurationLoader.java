package cash_flow.scene;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
@Slf4j
public class SceneConfigurationLoader {

    private final SceneConfiguration sceneConfiguration;

    public SceneConfigurationLoader(SceneConfiguration sceneConfiguration) {
        this.sceneConfiguration = sceneConfiguration;
    }

    public SceneConfiguration load(SceneType type) {
        if (type == null) {
            log.error("SceneType is null, cannot load scene properties for null type");
            throw new IllegalArgumentException("SceneType must not be null");
        }
        Properties props = new Properties();
        try (InputStream input = getClass().getResourceAsStream(type.getPropertiesFilePath())) {
            props.load(input);
            sceneConfiguration.fillSceneConfig(props);
            return sceneConfiguration;
        } catch (IOException e) {
            // Log the error or handle it as needed
            log.error("Error loading scene properties for type: {}", type);
            throw new RuntimeException("Failed to load scene properties", e);
        }
    }
}
