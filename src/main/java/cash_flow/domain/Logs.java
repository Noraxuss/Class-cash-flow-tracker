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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "logs_messages")
    private LogsMessages logsMessages;

    // optional free-text for extra notes (admin corrections etc.)
    @Column(columnDefinition = "TEXT")
    private String note;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToOne(mappedBy = "logs")
    private GroupCurrencyHistory groupCurrencyHistory;

    // Parent log (nullable, because root logs won’t have a parent)
    @ManyToOne
    @JoinColumn(name = "parent_log_id")
    private Logs parentLog;

    // Child logs (refunds, corrections, etc.)
    @OneToMany(mappedBy = "parentLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Logs> relatedLogs = new ArrayList<>();
}
