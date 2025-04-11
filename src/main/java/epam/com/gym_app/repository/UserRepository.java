package epam.com.gym_app.repository;

import epam.com.gym_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select count(u.id) from User u where u.firstName = :firstName and u.lastName = :lastName")
    Integer countByFirstNameAndLastName(String firstName, String lastName);

    Optional<User> findByUserName(String username);
}
