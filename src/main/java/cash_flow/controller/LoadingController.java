package cash_flow.controller;

import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.scene.SceneEngine;
import cash_flow.scene.SceneType;
import cash_flow.service.StartupProgressService;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Setter
@Getter
public class LoadingController implements DeferredSceneInit, ThemeChangeListener {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label statusLabel;

    private final StartupProgressService startupProgressService;
    private final SceneEngine sceneEngine;
    private final StyleManager styleManager;
    private final ControllerUtilities controllerUtilities;

    @Autowired
    public LoadingController(StartupProgressService startupProgressService, SceneEngine sceneEngine, StyleManager styleManager, ControllerUtilities controllerUtilities) {
        this.startupProgressService = startupProgressService;
        this.sceneEngine = sceneEngine;
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
    }

    @FXML
    public void initialize() {
        progressBar.setProgress(0.0); // Initialize progress bar to 0

        controllerUtilities.initializeSceneStyle(statusLabel, this);
    }

    @FXML
    public synchronized void updateProgress(double progress, String message) {

            progressBar.setProgress(progressBar.getProgress() + progress);
            statusLabel.setText(message);
            log.info("Updating progress to [{}] with message [{}]", progressBar.getProgress(), message);

            if (progressBar.getProgress() >= 1.0) {
                sceneEngine.switchScene(SceneType.GROUP_CHOOSING);
            }


//        try {
//            Thread.sleep(1000); // Simulate a delay for loading
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public void onSceneLoad() {
        Platform.runLater(startupProgressService::checkProgramStartupProgress);
    }

    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(statusLabel.getScene(), this);
    }
}
