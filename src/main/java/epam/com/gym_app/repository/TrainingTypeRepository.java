package epam.com.gym_app.repository;

import epam.com.gym_app.entity.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Integer> {
    Optional<TrainingType> findTrainingTypeById(Integer id);

}
