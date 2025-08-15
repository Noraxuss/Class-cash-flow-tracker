package cash_flow.repository;

import cash_flow.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    @Query(value = "SELECT DISTINCT m FROM Member m " +
            "JOIN m.groupMemberships gm " +
            "WHERE gm.group.id = :groupId")
    List<Member> findAllByGroupId(@Param("groupId") Long groupId);

}
