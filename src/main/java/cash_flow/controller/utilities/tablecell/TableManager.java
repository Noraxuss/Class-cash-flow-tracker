package cash_flow.controller.utilities.tablecell;

import cash_flow.context.AppContext;
import cash_flow.dto.*;
import cash_flow.service.InUIValidationService;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Component
@Slf4j
public class TableManager {

    private final InUIValidationService inUIValidationService;
    private final AppContext appContext;

    public TableManager(InUIValidationService inUIValidationService, AppContext appContext) {
        this.inUIValidationService = inUIValidationService;
        this.appContext = appContext;
    }

    // For GroupMemberRowModel and its enum
    public void configureGroupMemberTable(
            TableView<GroupMemberRowModel> tableView,
            Supplier<GroupMemberRowModel> newRowSupplier
    ) {
        configureTable(
                tableView,
                GroupMemberRowModelEnum.MEMBER_EMAIL, // replace with your actual enum constant
                newRowSupplier
        );
    }

    // For PaymentRowModel and its enum
    public void configurePaymentTable(
            TableView<PaymentRowModel> tableView,
            Supplier<PaymentRowModel> newRowSupplier
    ) {
        configureTable(
                tableView,
                PaymentRowModelEnum.PAYMENT_AMOUNT, // replace with your actual enum constant
                newRowSupplier
        );
    }

    // Generic internal method reused by both above
    private <S, T, E extends Enum<E> & PropertyName<T, S>> void configureTable(
            TableView<S> tableView,
            E sampleEnumConstant,
            Supplier<S> newRowSupplier
    ) {
        Class<E> enumClass = sampleEnumConstant.getDeclaringClass();

        for (TableColumn<S, ?> column : tableView.getColumns()) {
            if (column == null || column.getText() == null) continue;

            createColumn((TableView<S>) tableView,
                    (Supplier<S>) newRowSupplier,
                    (Class<E>) enumClass,
                    (TableColumn<S, ?>) column);

            if (column.getId().equals("optional")) {
                for (TableColumn<S, ?> optionalsColum : column.getColumns()) {
                    createColumn((TableView<S>) tableView,
                            (Supplier<S>) newRowSupplier,
                            (Class<E>) enumClass,
                            (TableColumn<S, ?>) optionalsColum);
                }
            }

        }
    }

    private <S, T, E extends Enum<E> & PropertyName<T, S>> void createColumn(TableView<S> tableView, Supplier<S> newRowSupplier, Class<E> enumClass, TableColumn<S, ?> optionalsColum) {
        for (E constant : enumClass.getEnumConstants()) {
            if (constant.getPropertyName().equals(optionalsColum.getId())) {
                @SuppressWarnings("unchecked")
                TableColumn<S, T> typedColumn = (TableColumn<S, T>) optionalsColum;
                chooseColumnType(tableView, typedColumn, constant, newRowSupplier);
                break;
            }
        }
    }

    private <S, T, E extends Enum<E> & PropertyName<T, S>> void chooseColumnType(
            TableView<S> tableView,
            TableColumn<S, T> column,
            E constant,
            Supplier<S> newRowSupplier
    ) {
        switch (constant.getEditingType()) {
            case TEXT:
                setupTextColumn(tableView, column, constant, newRowSupplier);
                break;
            case NUMBER:
//                setupColumn(tableView, column, constant, newRowSupplier);
                break;
            case DATE_PICKER:
                setupDatePickerColumn(tableView, column, constant, newRowSupplier);
                break;
            case DROPDOWN:
//                setupColumn(tableView, column, constant, newRowSupplier);
                break;
            default:
                throw new IllegalArgumentException("Unsupported editing type: " + constant.getEditingType());
        }
    }

    private <S, T, E extends Enum<E> & PropertyName<T, S>> void setupTextColumn(
            TableView<S> tableView,
            TableColumn<S, T> column,
            E constant,
            Supplier<S> newRowSupplier
    ) {
        column.setCellValueFactory(new PropertyValueFactory<>(constant.getPropertyName()));
        StringConverter<T> converter = getConverterForClass(constant.getClassType());

        column.setCellFactory(param -> new EditableTextCell<>(
                converter,
                inUIValidationService,
                constant));

        column.setOnEditCommit(event -> {
            handleEditCommitWithAutoRow(
                    event,
                    constant::setValue,
                    tableView.getItems(),
                    this::isRowNotEmpty,
                    newRowSupplier
            );
        });
    }

    private <S, T, E extends Enum<E> & PropertyName<T, S>> void setupDatePickerColumn(
            TableView<S> tableView,
            TableColumn<S, T> column,
            E constant,
            Supplier<S> newRowSupplier
    ) {
        column.setCellValueFactory(new PropertyValueFactory<>(constant.getPropertyName()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        @SuppressWarnings("unchecked")
        TableColumn<S, LocalDate> dateColumn = (TableColumn<S, LocalDate>) column;

        dateColumn.setCellFactory(param -> new DatePickerTableCell<>(
                formatter,
                inUIValidationService));

        column.setOnEditCommit(event -> {
            handleEditCommitWithAutoRow(
                    event,
                    constant::setValue,
                    tableView.getItems(),
                    this::isRowNotEmpty,
                    newRowSupplier
            );
        });
    }

    /**
     * Returns a StringConverter based on the class type for editing support.
     */
    @SuppressWarnings("unchecked")
    private <T> StringConverter<T> getConverterForClass(Class<?> clazz) {
        if (clazz == String.class) {
            return (StringConverter<T>) new DefaultStringConverter();
        } else if (clazz == Integer.class) {
            return (StringConverter<T>) new IntegerStringConverter();
        } else if (clazz == LocalDate.class) {
            return (StringConverter<T>) new LocalDateStringConverter();
        }
        throw new IllegalArgumentException("No converter available for " + clazz);
    }

    /**
     * Generic handler for edit commits that updates the model, and if the edited row
     * is the last row and non-empty, adds a new empty row.
     */
    public <S, T> void handleEditCommitWithAutoRow(
            TableColumn.CellEditEvent<S, T> event,
            BiConsumer<S, T> propertySetter,
            ObservableList<S> items,
            Predicate<S> isRowNotEmpty,
            Supplier<S> newRowSupplier
    )
    {
        S editedItem = event.getRowValue();
        T newValue = event.getNewValue();

        propertySetter.accept(editedItem, newValue);

        boolean isLastRow = items.indexOf(editedItem) == items.size() - 1;

        if (isLastRow && isRowNotEmpty.test(editedItem)) {
            Platform.runLater(() -> {
                if (items.stream().noneMatch(isRow -> !isRowNotEmpty.test(isRow))) {
                    items.add(newRowSupplier.get());
                }
            });
        }
    }

    /**
     * Example check if row is non-empty. Adjust this to fit your row model.
     */
    private boolean isRowNotEmpty(Object row) {
        if (!(row instanceof GroupMemberRowModel model)) return false;

        String first = model.getMemberFirstName().get();
        String last = model.getMemberLastName().get();
        String email = model.getMemberEmail().get();

        return (first != null && !first.isEmpty())
                || (last != null && !last.isEmpty())
                || (email != null && !email.isEmpty());
    }

    /**
     * Creates a new empty row with any necessary default values.
     */
    public GroupMemberRowModel newEmptyGroupMemberRowWithDefaults() {
        GroupMemberRowModel newRow = new GroupMemberRowModel();
        LocalDate startDate = appContext.getGroupContext().getStartDate();
        log.info("Creating new empty group member row with defaults {}", startDate);
        newRow.setMemberJoinDate(startDate);
        return newRow;
    }

    /**
     * Creates a new empty payment row with any necessary default values.
     */
    public PaymentRowModel newEmptyPaymentRowWithDefaults() {
        PaymentRowModel newRow = new PaymentRowModel();
        newRow.setPaymentDate(LocalDate.now());
        return newRow;
    }

    /**
     * Moves the editing focus to the next editable cell, adding new row if necessary.
     */
    public <S> void moveToNextEditableCell(
            TableView<S> tableView,
            TableColumn<S, ?> excludeColumn,
            ObservableList<S> backingList,
            Supplier<S> newRowSupplier
    ) {
        var focusModel = tableView.getFocusModel();
        var pos = focusModel.getFocusedCell();

        int currentRow = pos.getRow();
        int currentCol = pos.getColumn();

        log.debug("Current focus: row={}, col={}, column={}", currentRow, currentCol,
                pos.getTableColumn() != null ? pos.getTableColumn().getText() : "null");

        // Use visible leaf columns to avoid child-column issues
        List<TableColumn<S, ?>> columns = new ArrayList<>(tableView.getVisibleLeafColumns());

        log.debug("Visible columns: {}", columns.stream()
                .map(col -> col.getText() + " (editable=" + col.isEditable() + ")")
                .toList());

        int nextCol = currentCol + 1;
        int nextRow = currentRow;

        if (nextCol >= columns.size()) {
            nextCol = 0;
            nextRow++;
        }

        if (nextRow >= backingList.size()) {
            log.debug("Adding new row at index {}", backingList.size());
            backingList.add(newRowSupplier.get());
        }

        final int targetRow = nextRow;
        final int targetCol = nextCol;

        TableColumn<S, ?> targetColumn = columns.get(targetCol);

        log.debug("Attempting to move to row={}, col={}, column={}", targetRow, targetCol,
                targetColumn.getText());

        Platform.runLater(() -> {
            tableView.requestFocus();

            tableView.scrollTo(targetRow);
            tableView.scrollToColumn(targetColumn);
            tableView.getSelectionModel().clearAndSelect(targetRow, targetColumn);
            tableView.getFocusModel().focus(targetRow, targetColumn);

            if (!targetColumn.isEditable()) {
                log.warn("Target column '{}' is not editable!", targetColumn.getText());
            }

            log.debug("Calling edit on row={}, column={}", targetRow, targetColumn.getText());
            tableView.edit(targetRow, targetColumn);
        });
    }

}
