package cash_flow.repository;

import cash_flow.domain.Guardian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuardianRepository extends JpaRepository<Guardian, String> {

    /**
     * Finds a Guardian by their email, first name, and last name.
     *
     * @param firstName the first name of the Guardian
     * @param lastName  the last name of the Guardian
     * @param email     the email of the Guardian
     * @return an Optional containing the Guardian if found, or empty if not found
     */
    @Query("SELECT g FROM Guardian g WHERE g.firstName = :firstName " +
            "AND g.lastName = :lastName " +
            "AND g.email = :email")
    Optional<Guardian> findByEmailAndFirstNameAndLastName(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email
    );
}
