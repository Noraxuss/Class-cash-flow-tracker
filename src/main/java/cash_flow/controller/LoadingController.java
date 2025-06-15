package cash_flow.controller;

import cash_flow.scene.SceneEngine;
import cash_flow.service.StartupProgressService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Setter
@Getter
public class LoadingController {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label statusLabel;

    private final StartupProgressService startupProgressService;
    private final SceneEngine sceneEngine;

    @Autowired
    public LoadingController(StartupProgressService startupProgressService, SceneEngine sceneEngine) {
        this.startupProgressService = startupProgressService;
        this.sceneEngine = sceneEngine;
    }

    @FXML
    public void initialize() {
        try {
            Thread.sleep(1000); // Simulate a delay for loading
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        startupProgressService.checkProgramStartupProgress();
        if(progressBar.getProgress() == 1.0) {
            sceneEngine.switchScene("choose_cash_collection_group_scene");
        }
    }

    @FXML
    public synchronized void updateProgress(double progress, String message) {
        progressBar.setProgress(progressBar.getProgress() + progress); // Update the progress bar
        statusLabel.setText(message); // Update the status label
        log.info("Updating progress to [{}] with message [{}]", progressBar.getProgress(), message);

    }
}
