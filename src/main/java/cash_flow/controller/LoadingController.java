package cash_flow.controller;

import cash_flow.context.AppContext;
import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.scene.SceneEngine;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Setter
@Getter
public class LoadingController implements ThemeChangeListener {

    @FXML
    public ProgressIndicator spinner;

    @FXML
    public Label progressLabel;

    @FXML
    public Label currentStepLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label statusLabel;

    private final SceneEngine sceneEngine;
    private final StyleManager styleManager;
    private final ControllerUtilities controllerUtilities;
    private final AppContext appContext;


    @Autowired
    public LoadingController(SceneEngine sceneEngine,
                             StyleManager styleManager,
                             ControllerUtilities controllerUtilities, AppContext appContext) {
        this.sceneEngine = sceneEngine;
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
        this.appContext = appContext;
    }

    @FXML
    public void initialize() {
        controllerUtilities.initializeSceneStyle(statusLabel, this);
        showSpinnerLoading();
        currentStepLabel.setManaged(false);
        currentStepLabel.setVisible(false);
    }

    public void showSpinnerLoading() {
        log.info("Showing spinner loading animation");
        // Show the spinner
        spinner.setManaged(true);
        spinner.setVisible(true);
        // Hide the progress bar
        progressBar.setManaged(false);
        progressBar.setVisible(false);
        // Hide the progress label
        progressLabel.setVisible(false);
        progressLabel.setManaged(false);
        // Hide the current step label
        currentStepLabel.setManaged(false);
        currentStepLabel.setVisible(false);
    }

    public void showProgressBarLoading() {
        log.info("Showing progress bar loading animation");
        // Hide the spinner
        spinner.setManaged(false);
        spinner.setVisible(false);
        // Show the progress bar and label
        progressBar.setManaged(true);
        progressBar.setVisible(true);
        // Show the progress label
        progressLabel.setVisible(true);
        progressLabel.setManaged(true);
        // Hide the current step label
        currentStepLabel.setManaged(true);
        currentStepLabel.setVisible(true);
    }

    public void updateProgressLabel(String text) {
        progressLabel.setText(text);
    }

    public void updateCurrentStepLabel(String text) {
        currentStepLabel.setText(text);
    }

    public void updateProgress(double progress) {
        Platform.runLater(() -> {
            progressBar.setProgress(progress);
            if (progress >= 1.0) {
                statusLabel.setText("Done!");
            }
        });
    }

    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(statusLabel.getScene(), this);
    }
}
