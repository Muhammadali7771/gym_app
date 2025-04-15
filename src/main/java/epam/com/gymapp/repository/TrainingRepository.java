package epam.com.gymapp.repository;

import epam.com.gymapp.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Integer> {
    @Query("""
            select t from Training t where t.trainee.user.userName = :traineeUsername 
            and (cast(:fromDate as date) is null or t.trainingDate >= :fromDate)
            and (cast(:toDate as date) is null or t.trainingDate <= :toDate)
            and (:trainerName is null or t.trainer.user.firstName = :trainerName)
            and (:trainingTypeId is null or t.trainingType.id = :trainingTypeId)
            """)
    List<Training> getTraineeTrainingsListByTraineeUsernameAndCriteria(String traineeUsername,Date fromDate, Date toDate, String trainerName, Integer trainingTypeId);

    @Query("""
           select t from Training t where t.trainer.user.userName = :trainerUsername 
           and (cast(:fromDate as date) is null or t.trainingDate >= :fromDate)
           and (cast(:toDate as date) is null or t.trainingDate <= :toDate) 
           and (:traineeName is null or t.trainee.user.firstName = :traineeName)
     """)
    List<Training> getTrainerTrainingsListByTrainerUsernameAndCriteria(String trainerUsername, Date fromDate, Date toDate, String traineeName);
}
