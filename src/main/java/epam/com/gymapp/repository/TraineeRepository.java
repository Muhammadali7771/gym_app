package epam.com.gymapp.repository;


import epam.com.gymapp.entity.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Integer> {
    @Query("select count(t) > 0 from Trainee t where t.user.userName = :username and t.user.password = :password")
    boolean checkUsernameAndPasswordMatch(String username, String password);

    Optional<Trainee> findTraineeByUser_UserName(String username);

    @Modifying
    @Transactional
    @Query("""
                   update User u set u.password = :password
                   where u.userName = :username
            """)
    void changePassword(String username, String password);

    @Modifying
    @Transactional
    @Query("update User u set u.isActive = not u.isActive where u.userName = :username")
    void activateOrDeactivateTrainee(String username);

    @Modifying
    @Transactional
    void deleteByUser_UserName(String username);

    @Query("select t.id from Trainee t where t.user.userName = :username")
    Optional<Integer> findTraineeIdByUsername(String username);

    boolean existsByUser_UserName(String username);
}
