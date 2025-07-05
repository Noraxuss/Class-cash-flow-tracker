package cash_flow.repository;

import cash_flow.domain.Overseer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OverseerRepository extends JpaRepository<Overseer,String> {

    @Query(value = "SELECT EXISTS (" +
            "SELECT 1 FROM overseers " +
            "WHERE first_name = :firstName " +
            "AND last_name = :lastName " +
            "AND email = :email)", nativeQuery = true)
    boolean existsByFirstNameAndLastNameAndEmail(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email);

}
