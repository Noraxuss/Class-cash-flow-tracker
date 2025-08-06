package cash_flow.dto;

import javafx.beans.property.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PaymentRowModel {

    // Payment information fields
    private final StringProperty paymentMaker = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> paymentDate = new SimpleObjectProperty<>();
    private final StringProperty paymentGoal = new SimpleStringProperty();
    private final IntegerProperty paymentAmount = new SimpleIntegerProperty();
    private final StringProperty currency = new SimpleStringProperty();

    // === Property Getters ===
    public StringProperty paymentMakerProperty() {
        return paymentMaker;
    }
    public ObjectProperty<LocalDate> paymentDateProperty() {
        return paymentDate;
    }
    public StringProperty paymentGoalProperty() {
        return paymentGoal;
    }
    public IntegerProperty paymentAmountProperty() {
        return paymentAmount;
    }
    public StringProperty currencyProperty() {
        return currency;
    }

    // === Setters ===
    public void setPaymentMaker(String paymentMaker) {
        this.paymentMaker.set(paymentMaker);
    }
    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate.set(paymentDate);
    }
    public void setPaymentGoal(String paymentGoal) {
        this.paymentGoal.set(paymentGoal);
    }
    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount.set(paymentAmount);
    }
    public void setCurrency(String currency) {
        this.currency.set(currency);
    }

}
