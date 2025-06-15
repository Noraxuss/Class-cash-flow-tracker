package cash_flow.utilities;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;

/**
 * Provides individual startup checks. Does NOT manage execution threads.
 */
@Slf4j
@Component
public class StartupProgressListener {

    private final DataSource dataSource;

    public StartupProgressListener(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Check if a database can be connected to.
     */
    public boolean isDatabaseReady() {
        try (Connection conn = dataSource.getConnection()) {
            return conn.isValid(1000);
        } catch (Exception e) {
            log.error("Database connection failed", e);
            return false;
        }
    }

    /**
     * Check if required tables exist.
     */
    public boolean isFirstRun() {
        try (Connection conn = dataSource.getConnection()) {
            ResultSet tables = conn.getMetaData().getTables(null, null, "CASH_FLOW", null);
            return !tables.next();
        } catch (Exception e) {
            log.warn("Failed to detect first run. Defaulting to true.", e);
            return true;
        }
    }

    /**
     * Simulate loading settings (optional).
     */
    public boolean loadUserSettings() {
        try {
            Thread.sleep(200); // simulate work
            return true;
        } catch (InterruptedException e) {
            log.warn("Interrupted during settings load", e);
            return false;
        }
    }

    /**
     * Simulate component initialization (optional).
     */
    public boolean initializeUIComponents() {
        try {
            Thread.sleep(200); // simulate work
            return true;
        } catch (InterruptedException e) {
            log.warn("Interrupted during UI init", e);
            return false;
        }
    }
}
