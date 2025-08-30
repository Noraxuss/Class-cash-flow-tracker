package cash_flow.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "description")
    private String description;

    @Column(name = "group_creation_date", nullable = false)
    private LocalDateTime groupCreationDate;

    @Column(name = "group_end_date" , nullable = false)
    private LocalDateTime groupEndDate;

    @Column(name = "group_event_type")
    @Enumerated(EnumType.STRING)
    private GroupEventType groupEventType;

    @ManyToOne
    @JoinColumn(name = "overseer_id", nullable = false)
    private Overseer overseer;

    @OneToMany(mappedBy = "group")
    private List<GroupMembership> groupMemberships;

    @OneToMany(mappedBy = "group")
    private List<Logs> logs;

    @OneToMany(mappedBy = "group")
    private List<GroupCurrencyHistory> groupCurrencyHistory;

}
