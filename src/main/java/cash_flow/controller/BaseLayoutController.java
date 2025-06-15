package cash_flow.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
@Slf4j
public class BaseLayoutController {

    @FXML
    public MenuBar mainMenuBar;

    @FXML
    public MenuBar leftSubMenuBar;

    @FXML
    public TreeView collectionGroupDataTree;

    @FXML
    public VBox rightContentPane;

    @FXML
    public void initialize() {
        log.info("BaseLayoutController initialized");
        // Additional initialization logic can be added here if needed
    }

    public void setRightContentPane(Node rightContentPane) {
        this.rightContentPane.getChildren().setAll(rightContentPane);
    }
}
