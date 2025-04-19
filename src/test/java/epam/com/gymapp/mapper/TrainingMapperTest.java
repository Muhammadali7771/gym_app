package epam.com.gymapp.mapper;

import epam.com.gymapp.dto.training.TrainingCreateDto;
import epam.com.gymapp.dto.training.TrainingDto;
import epam.com.gymapp.entity.*;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class TrainingMapperTest {

    @Autowired
    private TrainingMapper trainingMapper;

    @Test
    void toEntity() {
        String traineeUsername = "Ali.Valiyev";
        String trainerUsername = "John.Doe";
        String trainingName = "first training";
        Date trainingDate = new Date(2025, 5, 5);
        Integer trainingTypeId = 1;
        Number trainingDuration = 50;
        TrainingCreateDto trainingCreateDto = new TrainingCreateDto(
                traineeUsername, trainerUsername, trainingName, trainingDate,
                trainingTypeId, trainingDuration
        );
        Trainer trainer = new Trainer();
        Trainee trainee = new Trainee();
        TrainingType trainingType = new TrainingType();

        Training training = trainingMapper.toEntity(trainingCreateDto, trainer, trainee, trainingType);

        Assertions.assertEquals(trainingName, training.getTrainingName());
        Assertions.assertEquals(trainingDate, training.getTrainingDate());
        Assertions.assertEquals(trainingDuration, training.getDuration());
        Assertions.assertEquals(trainingType, training.getTrainingType());
        Assertions.assertEquals(trainee, training.getTrainee());
        Assertions.assertEquals(trainer, training.getTrainer());
    }

    @Test
    void toEntity_2(){
        TrainingCreateDto trainingCreateDto = null;

        Training training = trainingMapper.toEntity(trainingCreateDto, null, null, null);

        Assertions.assertNull(training);
    }

    @Test
    void toDto() {
        Training training = new Training();
        String trainingName = "new training";
        Date date = new Date(2025, 4, 4);
        Number duration = 30;
        TrainingType trainingType = new TrainingType();
        trainingType.setId(2);
        Trainee trainee = new Trainee();
        User user = new User();
        String userName = "Ali";
        user.setUserName(userName);
        trainee.setUser(user);
        Trainer trainer = new Trainer();
        User user1 = new User();
        String userName2 = "John";
        user1.setUserName(userName2);
        trainer.setUser(user1);
        training.setTrainingName(trainingName);
        training.setTrainingDate(date);
        training.setTrainingType(trainingType);
        training.setDuration(duration);
        training.setTrainee(trainee);
        training.setTrainer(trainer);

        TrainingDto dto = trainingMapper.toDto(training);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(trainingName, dto.trainingName());
        Assertions.assertEquals(date, dto.trainingDate());
        Assertions.assertEquals(2, dto.trainingTypeId());
        Assertions.assertEquals(30, dto.trainingDuration());
        Assertions.assertEquals(userName, dto.traineeUsername());
        Assertions.assertEquals(userName2, dto.trainerUsername());
    }

    @Test
    void toDto_2(){
        Training training = null;

        TrainingDto dto = trainingMapper.toDto(null);

        Assertions.assertNull(dto);
    }

    @Test
    void toDtoList() {
        Training training = new Training();
        String trainingName = "new training";
        Date date = new Date(2025, 4, 4);
        Number duration = 30;
        TrainingType trainingType = new TrainingType();
        trainingType.setId(2);
        Trainee trainee = new Trainee();
        User user = new User();
        String userName = "Ali";
        user.setUserName(userName);
        trainee.setUser(user);
        Trainer trainer = new Trainer();
        User user1 = new User();
        String userName2 = "John";
        user1.setUserName(userName2);
        trainer.setUser(user1);
        training.setTrainingName(trainingName);
        training.setTrainingDate(date);
        training.setTrainingType(trainingType);
        training.setDuration(duration);
        training.setTrainee(trainee);
        training.setTrainer(trainer);

        List<TrainingDto> dtoList = trainingMapper.toDtoList(List.of(training));
        TrainingDto dto = dtoList.get(0);
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(trainingName, dto.trainingName());
        Assertions.assertEquals(date, dto.trainingDate());
        Assertions.assertEquals(2, dto.trainingTypeId());
        Assertions.assertEquals(30, dto.trainingDuration());
        Assertions.assertEquals(userName, dto.traineeUsername());
        Assertions.assertEquals(userName2, dto.trainerUsername());
    }
}