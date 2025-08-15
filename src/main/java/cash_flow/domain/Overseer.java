package cash_flow.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "overseers")
public class Overseer extends Person{

    @OneToMany(mappedBy = "overseer")
    private List<Member> members;

    @OneToMany(mappedBy = "overseer")
    private List<Group> groups;

}
