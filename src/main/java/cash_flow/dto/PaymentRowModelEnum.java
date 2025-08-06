package cash_flow.dto;

import lombok.Getter;
import java.time.LocalDate;
import java.util.function.BiConsumer;

@Getter
public enum PaymentRowModelEnum implements PropertyName<Object, PaymentRowModel> {

    PAYMENT_MAKER("paymentMakerColumn", String.class, EditingType.TEXT, (row, value) -> row.setPaymentMaker((String) value)),
    PAYMENT_DATE("paymentDateColumn", LocalDate.class, EditingType.DATE_PICKER, (row, value) -> row.setPaymentDate((LocalDate) value)),
    PAYMENT_GOAL("paymentGoalColumn", String.class, EditingType.TEXT, (row, value) -> row.setPaymentGoal((String) value)),
    PAYMENT_AMOUNT("paymentAmountColumn", Integer.class, EditingType.NUMBER, (row, value) -> row.setPaymentAmount((Integer) value)),
    CURRENCY("currencyColumn", String.class, EditingType.DROPDOWN, (row, value) -> row.setCurrency((String) value));

    private final String propertyName;
    private final Class<?> classType;
    private final EditingType editingType;
    private final BiConsumer<PaymentRowModel, Object> setter;

    PaymentRowModelEnum(String propertyName,
                        Class<?> classType,
                        EditingType editingType,
                        BiConsumer<PaymentRowModel, Object> setter) {
        this.propertyName = propertyName;
        this.classType = classType;
        this.editingType = editingType;
        this.setter = setter;
    }

    @Override
    public void setValue(PaymentRowModel row, Object value) {
        setter.accept(row, value);
    }
}
