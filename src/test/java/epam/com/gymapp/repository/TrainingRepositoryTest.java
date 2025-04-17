package epam.com.gymapp.repository;

import epam.com.gymapp.entity.Trainee;
import epam.com.gymapp.entity.Trainer;
import epam.com.gymapp.entity.Training;
import epam.com.gymapp.entity.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class TrainingRepositoryTest {
    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private TrainingTypeRepository trainingTypeRepository;
    @Autowired
    private TraineeRepository traineeRepository;

    @BeforeEach
    void setUp() {
        Trainee trainee1 = new Trainee();
        User user1 = new User();
        user1.setFirstName("Ali");
        user1.setLastName("Valiyev");
        user1.setUserName("Ali.Valiyev");
        user1.setPassword("111");
        user1.setIsActive(true);
        trainee1.setUser(user1);
        traineeRepository.save(trainee1);

        Trainer trainer1 = new Trainer();
        User user2 = new User();
        user2.setFirstName("Botir");
        user2.setLastName("Sobirov");
        user2.setUserName("Botir.Sobirov");
        user2.setPassword("222");
        user2.setIsActive(true);
        trainer1.setUser(user2);
        trainerRepository.save(trainer1);

        Training training1 = new Training();
        training1.setTrainingDate(new Date(2025, 03, 27));
        training1.setTrainingType(trainingTypeRepository.findTrainingTypeById(2).get());
        training1.setTrainingName("Training 1");
        training1.setDuration(90);
        training1.setTrainee(trainee1);
        training1.setTrainer(trainer1);
        trainingRepository.save(training1);
    }

    @AfterEach
    void tearDown() {
        trainingRepository.deleteAll();
        trainerRepository.deleteAll();
        traineeRepository.deleteAll();
    }

    @Test
    void getTraineeTrainingsListByTraineeUsernameAndCriteria() {
        String traineeUsername = "Ali.Valiyev";
        Date fromDate = new Date(2025, 03, 22);
        String trainerName = "Botir";
        List<Training> trainings = trainingRepository.getTraineeTrainingsListByTraineeUsernameAndCriteria(traineeUsername, fromDate, null, trainerName, null);


        Assertions.assertEquals(1, trainings.size());
        Assertions.assertEquals(traineeUsername, trainings.get(0).getTrainee().getUser().getUserName());
        Assertions.assertEquals(trainerName, trainings.get(0).getTrainer().getUser().getFirstName());
    }
    @Test
    void getTrainerTrainingsListByTrainerUsernameAndCriteria() {
        String trainerUsername = "Botir.Sobirov";
        Date toDate = new Date(2025, 03, 29);
        String traineeName = "Ali";
        List<Training> trainings = trainingRepository.getTrainerTrainingsListByTrainerUsernameAndCriteria(trainerUsername, null, toDate, traineeName);

        Assertions.assertEquals(1, trainings.size());
        Assertions.assertEquals(trainerUsername, trainings.get(0).getTrainer().getUser().getUserName());
        Assertions.assertEquals(traineeName, trainings.get(0).getTrainee().getUser().getFirstName());
    }
}