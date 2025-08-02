package cash_flow.utilities;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;

@Component
@Slf4j
public class SafetyFileManager {

    private static final String DIRECTORY_PATH = "safety_files";

    public void checkDirectoryExistence() {
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                log.info("Safety directory created at: {}", DIRECTORY_PATH);
            } else {
                log.error("Failed to create safety directory at: {}", DIRECTORY_PATH);
            }
        } else {
            log.info("Safety directory already exists at: {}", DIRECTORY_PATH);
        }
    }

    public void createSafetyFile(String fileName) {
        checkDirectoryExistence();
        File safetyFile = new File(DIRECTORY_PATH, fileName);
        try {
            if (safetyFile.createNewFile()) {
                log.info("Safety file created: {}", safetyFile.getAbsolutePath());
            } else {
                log.warn("Safety file already exists: {}", safetyFile.getAbsolutePath());
            }
        } catch (Exception e) {
            log.error("Error creating safety file: {}", e.getMessage());
        }
    }

    public void appendToSafetyFile(String fileName, String content) {
        checkDirectoryExistence();
        File safetyFile = new File(DIRECTORY_PATH, fileName);
        try (FileWriter writer = new FileWriter(safetyFile, true)) {
            writer.write(content + System.lineSeparator());
            log.info("Content appended to safety file: {}", safetyFile.getAbsolutePath());
        } catch (Exception e) {
            log.error("Error appending to safety file: {}", e.getMessage());
        }
    }

}
