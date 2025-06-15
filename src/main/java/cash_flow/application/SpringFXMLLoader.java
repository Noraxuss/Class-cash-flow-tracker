package cash_flow.application;

import javafx.fxml.FXMLLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class SpringFXMLLoader {

  private final ApplicationContext context;

  public SpringFXMLLoader(ApplicationContext context) {
    this.context = context;
  }

  public FXMLLoader load(String fxmlPath) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
    ResourceBundle resourceBundle = ResourceBundle.getBundle("languages/messages_hu");
    loader.setResources(resourceBundle);

    loader.setControllerFactory(context::getBean); // Tell FXMLLoader to get controllers from Spring
    return loader;
  }

}
