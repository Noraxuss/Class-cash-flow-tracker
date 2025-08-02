package ui_tests;

import cash_flow.application.SplashScreen;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

class SplashScreenTest extends ApplicationTest {

    private SplashScreen splashScreen;

    @Override
    public void start(Stage stage) {
        // Setup TestFX stage (JavaFX thread already running)
        splashScreen = new SplashScreen();
    }

    @Test
    void givenSplashScreen_whenSplashScreenDotShowIsCalled_thenShowSplashScreen() {
        // Show splash screen
        interact(() -> splashScreen.show());

        // Assert UI components
        verifyThat(".label", hasText("Loading, please wait..."));
        verifyThat(".progress-indicator", Node::isVisible);

        // Close splash screen
        interact(() -> splashScreen.close());
    }
}
