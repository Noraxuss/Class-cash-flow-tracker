//package cash_flow.service;
//
//import cash_flow.controller.LoadingController;
//import cash_flow.utilities.StartupProgressListener;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//import java.util.concurrent.Callable;
//
///**
// * Manages the startup progress bar and executes tasks in parallel.
// */
//@Slf4j
//@Service
//public class StartupProgressService {
//
//    private final StartupProgressListener listener;
//    private final LoadingController loadingController;
//
//    public StartupProgressService(StartupProgressListener listener, @Lazy LoadingController loadingController) {
//        this.listener = listener;
//        this.loadingController = loadingController;
//    }
//
//    public void checkProgramStartupProgress() {
//        Map<String, Callable<Boolean>> checks = Map.of(
//                "Connecting to database...", listener::isDatabaseReady,
//                "Checking schema (first run?)...", listener::isFirstRun,
//                "Loading user settings...", listener::loadUserSettings,
//                "Initializing UI components...", listener::initializeUIComponents
//        );
//
//        updateProgress(loadingController.getProgressBar().getProgress(), "Starting application...");
//
//        int total = checks.size();
//        double progress = 1.0 / total;
//
//        for (Map.Entry<String, Callable<Boolean>> entry : checks.entrySet()) {
//            String description = entry.getKey();
//
//
//            updateProgress(progress ,"Completed: " + description);
//        }
//    }
//
//    /**
//     * Updates the progress bar on the JavaFX Application Thread.
//     */
//    private void  updateProgress(double progress, String message) {
//        loadingController.updateProgress(progress, message);
//    }
//
//}
