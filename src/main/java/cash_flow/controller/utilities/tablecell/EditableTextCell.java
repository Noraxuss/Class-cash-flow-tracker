package cash_flow.controller.utilities.tablecell;

import cash_flow.dto.GroupMemberRowModelEnum;
import cash_flow.service.InUIValidationService;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EditableTextCell<S> extends TableCell<S, String> {

    private final TextField textField = new TextField();
    private final StringConverter<String> converter;
    private final InUIValidationService inUIValidationService;
    private final GroupMemberRowModelEnum userData;

    public EditableTextCell(StringConverter<String> converter,
                            InUIValidationService inUIValidationService,
                            GroupMemberRowModelEnum userData) {
        this.converter = converter;
        this.inUIValidationService = inUIValidationService;
        this.userData = userData;


        // Commit value when Enter or Tab is pressed
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                commitEdit(converter.fromString(textField.getText()));
                event.consume(); // Prevent default focus shift
            } else if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
                event.consume();
            }
        });

        // Commit when focus is lost
        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && isEditing()) {
                commitEdit(converter.fromString(textField.getText()));
            }
        });
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                textField.setText(item);
                setGraphic(textField);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                setText(item);
                setGraphic(null);
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();
        textField.setText(getItem());
        setGraphic(textField);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        textField.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem());
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        setContentDisplay(ContentDisplay.TEXT_ONLY);

        String key = getIndex() + userData.getPropertyName();

        if ((newValue == null || newValue.isBlank())
                && (userData == GroupMemberRowModelEnum.GUARDIAN_EMAIL
                || userData == GroupMemberRowModelEnum.GUARDIAN_FIRST_NAME
                || userData == GroupMemberRowModelEnum.GUARDIAN_LAST_NAME)) {
            log.warn("Attempted to commit null value for {}", userData);
            inUIValidationService.deleteValidationError(key);
            return; // Prevent committing null values for guardian fields
        }

        // Validate input
        String validationError = switch (userData) {
            case MEMBER_FIRST_NAME, MEMBER_LAST_NAME,
                 GUARDIAN_FIRST_NAME, GUARDIAN_LAST_NAME -> inUIValidationService.validateName(newValue).orElse(null);

            case MEMBER_EMAIL, GUARDIAN_EMAIL -> inUIValidationService.validateEmail(newValue).orElse(null);

            default -> throw new IllegalArgumentException("Invalid user data");
        };

        if (validationError != null) {
            inUIValidationService.addValidationError(key, validationError);
        } else {
            inUIValidationService.deleteValidationError(key);
        }


        if (inUIValidationService.getValidationError(key) != null) {
            Tooltip tooltip = new Tooltip(inUIValidationService.getValidationError(newValue));
            tooltip.setStyle("-fx-text-fill: red;"); // Sets the text color to red
            tooltip.setAutoHide(true);
            tooltip.setShowDelay(javafx.util.Duration.ZERO);
            setTooltip(tooltip);
            log.error("Validation error: {}", inUIValidationService.getValidationError(newValue));

            // 🔴 Add red border to visually highlight invalid cell
            setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        } else {
            setTooltip(null); // Clear tooltip if no validation error
            setStyle("-fx-border-color: transparent; -fx-border-width: 0;"); // Reset style if valid
        }
    }
}
