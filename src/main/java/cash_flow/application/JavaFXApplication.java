package cash_flow.application;

import cash_flow.Main;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component

public class JavaFXApplication extends Application {

  private ApplicationContext springContext;

  @Override
  public void init() {
    // Initialize the Spring context
    SpringApplicationBuilder builder = new SpringApplicationBuilder(Main.class);
    builder.application().setWebApplicationType(WebApplicationType.NONE);
    builder.headless(false);
    springContext = builder.run();
  }

  @Override
  public void start(Stage primaryStage) {

  }

  @Override
  public void stop() {
    // Properly close the Spring context when JavaFX application exits
    ((org.springframework.context.ConfigurableApplicationContext) springContext).close();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
