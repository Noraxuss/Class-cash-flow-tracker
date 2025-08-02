package cash_flow.controller.utilities.tablecell;

import cash_flow.service.InUIValidationService;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A TableCell implementation that uses a DatePicker to edit LocalDate values.
 */
@Slf4j
public class DatePickerTableCell<S> extends TableCell<S, LocalDate> {

    private final DatePicker datePicker = new DatePicker();            // The embedded date picker
    private final DateTimeFormatter formatter;                         // Formatter for displaying date
    private final InUIValidationService inUIValidationService;        // (Optional) Date validation logic

    public DatePickerTableCell(DateTimeFormatter formatter, InUIValidationService inUIValidationService) {
        this.formatter = formatter;
        this.inUIValidationService = inUIValidationService;

        configureDatePicker();
        configureKeyboardEvents();
    }

    /**
     * Configures the date picker's string converter for formatting/parsing.
     */
    private void configureDatePicker() {
        datePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string == null || string.isEmpty()) return null;
                try {
                    return LocalDate.parse(string, formatter);
                } catch (Exception e) {
                    log.warn("Invalid date format: {}", string);
                    return null;
                }
            }
        });

        // Commit the selected value when a date is picked from the UI calendar
        datePicker.setOnAction(event -> commitEdit(datePicker.getValue()));
    }

    /**
     * Sets up keyboard behavior (Enter, Tab, Escape).
     */
    private void configureKeyboardEvents() {
        datePicker.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER, TAB -> {
                    // Commit and let the controller handle focus shifting
                    commitEdit(datePicker.getValue());
                    event.consume();
                }
                case ESCAPE -> {
                    cancelEdit();   // Cancel the edit operation
                    event.consume();
                }
            }
        });
    }

    /**
     * Updates the cell view (text or graphic depending on edit state).
     */
    @Override
    protected void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            // Clear everything if the cell is empty
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                // While editing: show DatePicker
                datePicker.setValue(item);
                setGraphic(datePicker);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                // When not editing: show formatted text
                setText(item != null ? formatter.format(item) : "");
                setGraphic(null);
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }
    }

    /**
     * Called when user begins editing the cell.
     */
    @Override
    public void startEdit() {
        super.startEdit();
        datePicker.setValue(getItem());
        setGraphic(datePicker);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        datePicker.requestFocus(); // Immediately focus for user input
    }

    /**
     * Cancels editing and returns to text view.
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }
}
