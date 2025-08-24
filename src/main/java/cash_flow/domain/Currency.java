package cash_flow.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "currency")
public class Currency {

    @Id
    @Column(nullable = false, name = "code")
    private String code;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(name = "symbol")
    private String symbol;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "currency")
    private RequiredPayment requiredPayment;



}
