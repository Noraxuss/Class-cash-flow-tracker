package cash_flow.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "guardians")
public class Guardian extends Person{

    @ManyToOne
    @JoinColumn(name = "group_member_id", nullable = false)
    private Member member;
}
