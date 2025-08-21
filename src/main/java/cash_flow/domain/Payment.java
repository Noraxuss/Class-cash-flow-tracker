package cash_flow.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long id;

    @Column(nullable = false, name = "amount")
    private Double amount;

    @Column(nullable = false, name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(nullable = false, name = "payment_event_type")
    @Enumerated(EnumType.STRING)
    private PaymentEventType paymentEventType;

    @ManyToOne
    @JoinColumn(name = "group_member_id", nullable = false)
    private Member member;

}
