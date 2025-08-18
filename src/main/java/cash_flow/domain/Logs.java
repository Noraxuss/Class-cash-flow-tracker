package cash_flow.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "logs")
public class Logs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long id;

    @Column(nullable = false, name = "message")
    private String message;

    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    // Parent log (nullable, because root logs won’t have a parent)
    @ManyToOne
    @JoinColumn(name = "parent_log_id")
    private Logs parentLog;

    // Child logs (refunds, corrections, etc.)
    @OneToMany(mappedBy = "parentLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Logs> relatedLogs = new ArrayList<>();

    // optional free-text for extra notes (admin corrections etc.)
    @Column(columnDefinition = "TEXT")
    private String note;
}
