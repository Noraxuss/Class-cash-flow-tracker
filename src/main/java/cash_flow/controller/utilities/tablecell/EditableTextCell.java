package cash_flow.controller.utilities.tablecell;

import cash_flow.dto.GroupMemberRowModel;
import cash_flow.dto.GroupMemberRowModelEnum;
import cash_flow.dto.PropertyName;
import cash_flow.service.InUIValidationService;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EditableTextCell<S, T> extends TableCell<S, T> {
    private final TextField textField = new TextField();
    private final StringConverter<T> converter;
    private final InUIValidationService inUIValidationService;
    private final PropertyName<T, S> userData;

    public EditableTextCell(StringConverter<T> converter,
                            InUIValidationService inUIValidationService,
                            PropertyName<T, S> userData) {
        this.converter = converter;
        this.inUIValidationService = inUIValidationService;
        this.userData = userData;

        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                commitEdit(converter.fromString(textField.getText()));
                event.consume();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
                event.consume();
            }
        });

        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && isEditing()) {
                commitEdit(converter.fromString(textField.getText()));
            }
        });
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                textField.setText(converter.toString(item));
                setGraphic(textField);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                setText(converter.toString(item));
                setGraphic(null);
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();
        textField.setText(converter.toString(getItem()));
        setGraphic(textField);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        textField.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(converter.toString(getItem()));
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void commitEdit(T newValue) {
        super.commitEdit(newValue);
        setContentDisplay(ContentDisplay.TEXT_ONLY);

        // Validation is only applied if this is a String column
        if (newValue instanceof String strVal) {
            String key = getIndex() + userData.getPropertyName();

            boolean isGuardianField = userData.getPropertyName().contains("guardian");

            if ((strVal == null || strVal.isBlank()) && isGuardianField) {
                log.warn("Attempted to commit null value for {}", userData.getPropertyName());
                inUIValidationService.deleteValidationError(key);
                return;
            }

            String validationError = null;



            switch (userData.getPropertyName()) {
                case GroupMemberRowModelEnum.MEMBER_FIRST_NAME, "lastNameColumn", "guardianFirstNameColumn", "guardianLastNameColumn" ->
                        validationError = inUIValidationService.validateName(strVal).orElse(null);

                case "emailColumn", "guardianEmailColumn" ->
                        validationError = inUIValidationService.validateEmail(strVal).orElse(null);
            }

            if (validationError != null) {
                inUIValidationService.addValidationError(key, validationError);
                Tooltip tooltip = new Tooltip(validationError);
                tooltip.setStyle("-fx-text-fill: red;");
                tooltip.setAutoHide(true);
                tooltip.setShowDelay(javafx.util.Duration.ZERO);
                setTooltip(tooltip);
                setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                log.error("Validation error: {}", validationError);
            } else {
                inUIValidationService.deleteValidationError(key);
                setTooltip(null);
                setStyle("-fx-border-color: transparent; -fx-border-width: 0;");
            }
        }
    }
}
