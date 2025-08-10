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
        String path = type.getPropertiesFilePath();

        try (InputStream input = getClass().getResourceAsStream(path)) {
            log.info("Loading scene properties from file {}", path);

            if (input == null) {
                log.error("Could not find properties file at path: {}", path);
                throw new IllegalStateException("Properties file not found: " + path);
            }

            props.load(input);
            sceneConfiguration.fillSceneConfig(props);
            return sceneConfiguration;

        } catch (IOException e) {
            log.error("Error loading scene properties for type: {}", type, e);
            throw new RuntimeException("Failed to load scene properties", e);
        }
    }

}
