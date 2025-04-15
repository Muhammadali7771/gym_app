package epam.com.gymapp.repository;

import epam.com.gymapp.entity.TrainingType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class TrainingTypeRepositoryTest {
    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Test
    void findTrainingTypeById_1() {
        Integer id = 1;
        Optional<TrainingType> trainingTypeOptional = trainingTypeRepository.findTrainingTypeById(id);
        Assertions.assertTrue(trainingTypeOptional.isPresent());
        TrainingType trainingType = trainingTypeOptional.get();
        Assertions.assertEquals(id, trainingType.getId());
        Assertions.assertEquals("Fitness", trainingType.getTrainingTypeName());
    }

    @Test
    void findTrainingTypeById_3() {
        Integer id = 3;
        Optional<TrainingType> trainingTypeOptional = trainingTypeRepository.findTrainingTypeById(id);
        Assertions.assertTrue(trainingTypeOptional.isPresent());
        TrainingType trainingType = trainingTypeOptional.get();
        Assertions.assertEquals(id, trainingType.getId());
        Assertions.assertEquals("Zumba", trainingType.getTrainingTypeName());
    }
}