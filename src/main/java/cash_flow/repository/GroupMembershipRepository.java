package cash_flow.repository;

import cash_flow.domain.GroupMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMembershipRepository extends JpaRepository<GroupMembership,Long> {

    @Query("SELECT gm FROM GroupMembership gm JOIN FETCH gm.member WHERE gm.member.id = :id")
    GroupMembership findByMemberId(@Param("id") String id);

}
