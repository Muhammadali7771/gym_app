package epam.com.gymapp.repository;

import epam.com.gymapp.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Integer> {
    @Query("""
            select count(t) > 0 from Trainer t where 
            t.user.userName = :username and t.user.password = :password 
            """)
    boolean checkUsernameAndPasswordMatch(String username, String password);

    Optional<Trainer> findTrainerByUser_UserName(String username);

    @Modifying
    @Transactional
    @Query("""
            update User u set u.password = :password
            where u.userName = :username
            """)
    void changePassword(String username, String password);

    boolean existsByUser_UserName(String username);

    @Modifying
    @Transactional
    @Query("update User u set u.isActive = not u.isActive where u.userName = :username")
    void activateOrDeactivateTrainer(String username);

    @Query(value = "select t.id from trainers t left join trainee_trainer tt on t.id = tt.trainer_id where tt.trainee_id is distinct from :traineeId", nativeQuery = true)
    List<Integer> findTrainersIdNotAssignedOnTraineeByTraineeId(Integer traineeId);

    @Query("select t from Trainer t where t.id in (:ids)")
    List<Trainer> findTrainersById(List<Integer> ids);

    @Query("select t from Trainer t where t.user.userName in (:trainers)")
    List<Trainer> findTrainersByUser_UserNameIn(List<String> trainers);
}
