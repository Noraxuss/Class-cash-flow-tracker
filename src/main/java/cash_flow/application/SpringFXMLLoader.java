package cash_flow.application;

import cash_flow.scene.SceneConfiguration;
import javafx.fxml.FXMLLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
@Slf4j
public class SpringFXMLLoader {

    private final ApplicationContext context;

    public SpringFXMLLoader(ApplicationContext context) {
        this.context = context;
    }

    public FXMLLoader load(SceneConfiguration configuration) {
        FXMLLoader loader = null;
        try {
            log.info("Loading Spring FXML configuration for scene: {}", configuration.toString());
            log.debug("Loading FXML for scene: {}", configuration.getFxml());
            loader = new FXMLLoader(getClass().getResource(configuration.getFxml()));
            log.debug("Loading resourceBundle for scene: {}", configuration.getMessages());
            ResourceBundle resourceBundle = ResourceBundle.getBundle(configuration.getMessages(), new Utf8Control());
            loader.setResources(resourceBundle);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            loader.setControllerFactory(context::getBean); // Tell FXMLLoader to get controllers from Spring
        } catch (Exception e) {
            // if the bean does not exists just skip it
        }
        return loader;
    }

}
