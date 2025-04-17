package epam.com.gymapp.repository;

import epam.com.gymapp.entity.Trainee;
import epam.com.gymapp.entity.Trainer;
import epam.com.gymapp.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class TrainerRepositoryTest {
    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private TraineeRepository traineeRepository;

    Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        User user = new User();
        user.setFirstName("Botir");
        user.setLastName("Sobirov");
        user.setUserName("Botir.Sobirov");
        user.setPassword("777");
        user.setIsActive(true);
        trainer.setUser(user);
        trainerRepository.save(trainer);
    }

    @AfterEach
    void tearDown() {
        trainerRepository.deleteAll();
        traineeRepository.deleteAll();
    }

    @Test
    void checkUsernameAndPasswordMatch() {
        boolean result1 = trainerRepository.checkUsernameAndPasswordMatch("Botir.Sobirov", "777");
        boolean result2 = trainerRepository.checkUsernameAndPasswordMatch("Botir.Sobirov", "111");
        boolean result3 = trainerRepository.checkUsernameAndPasswordMatch("botir.sobirov", "777");

        Assertions.assertTrue(result1);
        Assertions.assertFalse(result2);
        Assertions.assertFalse(result3);
    }

    @Test
    void findTrainerByUser_UserName() {
        Optional<Trainer> trainerOptional = trainerRepository.findTrainerByUser_UserName("Botir.Sobirov");
        Assertions.assertTrue(trainerOptional.isPresent());
        Trainer trainer1 = trainerOptional.get();

        Assertions.assertEquals("Botir.Sobirov", trainer1.getUser().getUserName());
        Assertions.assertEquals("777", trainer1.getUser().getPassword());
        Assertions.assertEquals("Botir", trainer1.getUser().getFirstName());
        Assertions.assertEquals("Sobirov", trainer1.getUser().getLastName());
    }

    @Test
    void changePassword() {
        trainerRepository.changePassword("Botir.Sobirov", "888");

        Optional<Trainer> trainerOptional = trainerRepository.findTrainerByUser_UserName("Botir.Sobirov");
        Assertions.assertTrue(trainerOptional.isPresent());

        Trainer trainer1 = trainerOptional.get();
        Assertions.assertEquals("888", trainer1.getUser().getPassword());
    }

    @Test
    void existsByUser_UserName() {
        boolean result1 = trainerRepository.existsByUser_UserName("Botir.Sobirov");
        boolean result2 = trainerRepository.existsByUser_UserName("Sarvar.Olimov");

        Assertions.assertTrue(result1);
        Assertions.assertFalse(result2);
    }

    @Test
    void activateOrDeactivateTrainer() {
        trainerRepository.activateOrDeactivateTrainer("Botir.Sobirov");

        Optional<Trainer> trainerOptional1 = trainerRepository.findTrainerByUser_UserName("Botir.Sobirov");
        Trainer trainer1 = trainerOptional1.get();
        Assertions.assertFalse(trainer1.getUser().getIsActive());

        trainerRepository.activateOrDeactivateTrainer("Botir.Sobirov");
        Optional<Trainer> trainerOptional2 = trainerRepository.findTrainerByUser_UserName("Botir.Sobirov");
        Trainer trainer2 = trainerOptional2.get();
        Assertions.assertTrue(trainer2.getUser().getIsActive());
    }

    @Test
    void findTrainersIdNotAssignedOnTraineeByTraineeId() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setFirstName("Ali");
        user.setLastName("Valiyev");
        user.setUserName("Ali.Valiyev");
        user.setPassword("777");
        user.setIsActive(true);
        trainee.setUser(user);


        Trainer trainer2 = new Trainer();
        User user2 = new User();
        user2.setFirstName("Qahramon");
        user2.setLastName("Toyirov");
        user2.setUserName("Qahramon.Toyirov");
        user2.setPassword("333");
        user2.setIsActive(true);
        trainer2.setUser(user2);
        trainerRepository.save(trainer2);

        Trainer trainer3 = new Trainer();
        User user3 = new User();
        user3.setFirstName("Shamshod");
        user3.setLastName("Nabiyev");
        user3.setUserName("Shamshod.Nabiyev");
        user3.setPassword("333");
        user3.setIsActive(true);
        trainer3.setUser(user3);
        trainerRepository.save(trainer3);

        trainee.setTrainers(List.of(trainer));
        traineeRepository.save(trainee);

        List<Integer> trainerIds = trainerRepository.findTrainersIdNotAssignedOnTraineeByTraineeId(trainee.getId());
        Assertions.assertEquals(2, trainerIds.size());
        Assertions.assertTrue(trainerIds.contains(trainer2.getId()));
        Assertions.assertTrue(trainerIds.contains(trainer3.getId()));

        trainee.setTrainers(List.of());
        traineeRepository.save(trainee);
    }

    @Test
    void findTrainersById() {
        Trainer trainer2 = new Trainer();
        User user = new User();
        user.setFirstName("Qahramon");
        user.setLastName("Toyirov");
        user.setUserName("Qahramon.Toyirov");
        user.setPassword("333");
        user.setIsActive(true);
        trainer2.setUser(user);
        trainerRepository.save(trainer2);

        List<Integer> ids = List.of(trainer.getId(), trainer2.getId());
        List<Trainer> trainers = trainerRepository.findTrainersById(ids);

        Assertions.assertEquals(2, trainers.size());
        Assertions.assertEquals("Botir.Sobirov", trainers.get(0).getUser().getUserName());
        Assertions.assertEquals("Qahramon.Toyirov", trainers.get(1).getUser().getUserName());
    }

    @Test
    void findTrainersByUser_UserNameIn() {
        Trainer trainer2 = new Trainer();
        User user = new User();
        user.setFirstName("Qahramon");
        user.setLastName("Toyirov");
        user.setUserName("Qahramon.Toyirov");
        user.setPassword("333");
        user.setIsActive(true);
        trainer2.setUser(user);
        trainerRepository.save(trainer2);

        List<String> names = List.of("Botir.Sobirov", "Qahramon.Toyirov");
        List<Trainer> trainers = trainerRepository.findTrainersByUser_UserNameIn(names);
        Assertions.assertEquals(2, trainers.size());
        Assertions.assertEquals("Botir.Sobirov", trainers.get(0).getUser().getUserName());
        Assertions.assertEquals("Qahramon.Toyirov", trainers.get(1).getUser().getUserName());
    }
}