package cash_flow.repository;

import cash_flow.domain.Group;
import cash_flow.domain.Overseer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findAllByOverseer(Overseer overseer);
}
