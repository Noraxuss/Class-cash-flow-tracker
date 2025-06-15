package cash_flow;

import cash_flow.application.JavaFXApplication;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        Application.launch(JavaFXApplication.class, args);
    }
}