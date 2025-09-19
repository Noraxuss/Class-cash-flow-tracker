package cash_flow.repository;

import cash_flow.domain.Logs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Logs, String> {

    @Query("SELECT l FROM Logs l WHERE l.member.id = :memberId AND l.group.id = :groupId")
    List<Logs> findAllByMemberId(@Param("memberId") String memberId, @Param("groupId") Long groupId);

}
