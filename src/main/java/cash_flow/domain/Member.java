package cash_flow.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "group_members")
public class Member extends Person {

    @ManyToOne
    @JoinColumn(name = "overseer_id", nullable = false)
    private Overseer overseer;

    @OneToMany
    private List<Guardian> guardians;

    @OneToMany
    private List<GroupMembership> groupMemberships;

    @OneToMany
    private List<RequiredPayment> requiredPayments;

    @OneToMany
    private List<Payment> payments;

}
