package cash_flow.application;

import cash_flow.Main;
import cash_flow.scene.SceneEngine;
import cash_flow.scene.SceneType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Entry point for the JavaFX + Spring Boot hybrid application.
 * Initializes a temporary splash screen, then loads Spring context in the background.
 */
@Component
@Slf4j
public class JavaFXApplication extends Application {

    private ApplicationContext springContext;
    private SplashScreen splashScreen;

    @Override
    public void start(Stage primaryStage) {
        splashScreen = new SplashScreen(); // Step 1: show a loading splash before Spring starts
        splashScreen.show();

        // Load Spring context in background to keep UI responsive
        new Thread(() -> {
            initSpringContext();

            Platform.runLater(() -> {
                splashScreen.close(); // Close splash once Spring is ready

                SceneEngine sceneEngine = springContext.getBean(SceneEngine.class);
                sceneEngine.setMainStage(primaryStage);

                // Step 2: Initialize the main scene
                sceneEngine.initializeStage(SceneType.BASE);
            });
        }).start();
    }

    private void initSpringContext() {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Main.class);
        builder.application().setWebApplicationType(WebApplicationType.NONE);
        builder.headless(false);
        springContext = builder.run();
    }

    @Override
    public void stop() {
        // Gracefully shut down Spring context
        if (springContext != null) {
            ((org.springframework.context.ConfigurableApplicationContext) springContext).close();
        }
    }

    public static void main(String[] args) {
        launch(args); // Launches JavaFX runtime
    }
}
