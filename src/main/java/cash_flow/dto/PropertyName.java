package cash_flow.dto;

public interface PropertyName<T, R>  {

    String getPropertyName();

    Class<?> getClassType();

    EditingType getEditingType();

    void setValue(R row, T value);
}
