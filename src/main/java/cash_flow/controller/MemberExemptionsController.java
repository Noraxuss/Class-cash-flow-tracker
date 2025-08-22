package cash_flow.controller;

import cash_flow.context.AppContext;
import cash_flow.controller.utilities.ControllerUtilities;
import cash_flow.controller.utilities.TabManager;
import cash_flow.dto.outgoing.MemberExemptionDetails;
import cash_flow.scene.SceneEngine;
import cash_flow.service.MemberService;
import cash_flow.style_manager.Style;
import cash_flow.style_manager.StyleManager;
import cash_flow.style_manager.ThemeChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MemberExemptionsController implements ThemeChangeListener {

    private final StyleManager styleManager;
    private final ControllerUtilities controllerUtilities;
    private final SceneEngine sceneEngine;
    private final TabManager tabManager;
    private final AppContext appContext;

    private final MemberService memberService;

    @FXML public TextField searchBar;
    @FXML public ScrollPane exemptionsScrollPane;
    @FXML public VBox exemptionsVBox;
    @FXML public Label noResultsLabel;

    @Getter
    private ObservableList<MemberExemptionDetails> exemptions;

    public MemberExemptionsController(StyleManager styleManager, ControllerUtilities controllerUtilities, SceneEngine sceneEngine, TabManager tabManager, AppContext appContext, MemberService memberService) {
        this.styleManager = styleManager;
        this.controllerUtilities = controllerUtilities;
        this.sceneEngine = sceneEngine;
        this.tabManager = tabManager;
        this.appContext = appContext;
        this.memberService = memberService;
    }

    @FXML
    public void initialize() {
        log.info("Initializing MemberExemptionsController");
        controllerUtilities.initializeSceneStyle(searchBar, this);

        populateExemptionsList();
        setupSearchBar();
    }

    private void populateExemptionsList() {
        exemptions = FXCollections.observableArrayList();
        exemptions.addAll(memberService.getMemberList());

        exemptionsVBox.getChildren().clear(); // clear any previous nodes

        for (MemberExemptionDetails exemption : exemptions) {
            CheckBox checkBox = new CheckBox(exemption.getName());
            checkBox.setId(exemption.getId());
            exemptionsVBox.getChildren().add(checkBox);
        }

        // Add the "no results" label at the end, hidden initially
        noResultsLabel.setVisible(false);
        exemptionsVBox.getChildren().add(noResultsLabel);
    }

    private void setupSearchBar() {
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchText = newValue.toLowerCase();
            boolean anyVisible = false;

            for (var node : exemptionsVBox.getChildren()) {
                if (node instanceof CheckBox checkBox) {
                    boolean matches = checkBox.getText().toLowerCase().contains(searchText);
                    checkBox.setVisible(matches);
                    checkBox.setManaged(matches);
                    if (matches) anyVisible = true;
                }
            }

            noResultsLabel.setVisible(!anyVisible);
            noResultsLabel.setManaged(!anyVisible);
        });
    }
    
    @Override
    public void onThemeChanged(Style newTheme) {
        styleManager.toggleSceneStyle(searchBar.getScene(), this);
    }
}
