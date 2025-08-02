package cash_flow.application;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Simple JavaFX splash screen displayed before Spring is initialized.
 * Non-blocking but modal to prevent interaction with other windows.
 */
public class SplashScreen {

    private Stage stage;

    public void show() {
        Platform.runLater(() -> {
            Label loadingLabel = new Label("Loading, please wait...");
            ProgressIndicator indicator = new ProgressIndicator();

            VBox root = new VBox(15, loadingLabel, indicator);
            root.setAlignment(Pos.CENTER);
            Scene scene = new Scene(root, 300, 150);

            stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.show();
        });
    }

    public void close() {
        Platform.runLater(() -> {
            if (stage != null) {
                stage.close();
            }
        });
    }
}
