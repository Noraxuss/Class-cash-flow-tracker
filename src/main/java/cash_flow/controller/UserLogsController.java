package cash_flow.controller;

import cash_flow.context.AppContext;
import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.controller.utilities.TabManager;
import cash_flow.scene.SceneEngine;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserLogsController implements ThemeChangeListener {

    private final StyleManager styleManager;
    private final ControllerUtilities controllerUtilities;
    private final SceneEngine sceneEngine;
    private final TabManager tabManager;
    private final AppContext appContext;

    @FXML
    public Label titleLabel;

    @FXML
    public ListView<String> logsListView;

    public ObservableList<String> logsListObservable = FXCollections.observableArrayList();

    public UserLogsController(StyleManager styleManager, ControllerUtilities controllerUtilities, SceneEngine sceneEngine, TabManager tabManager, AppContext appContext) {
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
        this.sceneEngine = sceneEngine;
        this.tabManager = tabManager;
        this.appContext = appContext;
    }

    @FXML
    public void initialize() {
        controllerUtilities.initializeSceneStyle(titleLabel, this);

        logsListView.setItems(logsListObservable);

        logsListView.setCellFactory(lv -> new ListCell<String>() {
            private final Text text = new Text();

            {
                text.wrappingWidthProperty().bind(logsListView.widthProperty().subtract(20)); // padding
                setGraphic(text);
            }

            @Override
            protected void updateItem(String logMessage, boolean empty) {
                super.updateItem(logMessage, empty);
                if (empty || logMessage == null) {
                    text.setText(null);
                } else {
                    text.setText(logMessage);
                }
            }
        });

    }

    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(titleLabel.getScene(), this);
    }
}
