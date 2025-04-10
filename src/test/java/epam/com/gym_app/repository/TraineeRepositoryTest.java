package epam.com.gym_app.repository;

import epam.com.gym_app.entity.Trainee;
import epam.com.gym_app.entity.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Optional;

@SpringBootTest
class TraineeRepositoryTest {
    @Autowired
    private TraineeRepository traineeRepository;

    Trainee trainee;
    @BeforeEach
    void setUp() {
        trainee = new Trainee();
        User user = new User();
        user.setFirstName("Ali");
        user.setLastName("Valiyev");
        user.setUserName("Ali.Valiyev");
        user.setPassword("111");
        user.setActive(true);
        trainee.setUser(user);
        trainee.setAddress("Tashkent");
        trainee.setDateOfBirth(new Date());
        traineeRepository.save(trainee);
    }

    @AfterEach
    void tearDown() {
        traineeRepository.deleteAll();
    }

    @Test
    void checkUsernameAndPasswordMatch() {
        boolean result1 = traineeRepository.checkUsernameAndPasswordMatch("Ali.Valiyev", "111");
        boolean result2 = traineeRepository.checkUsernameAndPasswordMatch("Ali.Valiyev", "222");

        Assertions.assertTrue(result1);
        Assertions.assertFalse(result2);
    }

    @Test
    void findTraineeByUser_UserName_Exist() {
        Optional<Trainee> traineeOptional = traineeRepository.findTraineeByUser_UserName("Ali.Valiyev");

        Assertions.assertTrue(traineeOptional.isPresent());
        Trainee trainee1 = traineeOptional.get();
        Assertions.assertEquals("Ali.Valiyev", trainee1.getUser().getUserName());
        Assertions.assertEquals("Ali", trainee1.getUser().getFirstName());
        Assertions.assertEquals("Valiyev", trainee1.getUser().getLastName());
        Assertions.assertEquals("Tashkent", trainee1.getAddress());
    }

    @Test
    void findTraineeByUser_UserName_NotExist() {
        Optional<Trainee> traineeOptional = traineeRepository.findTraineeByUser_UserName("Sarvar.Sobirov");

        Assertions.assertFalse(traineeOptional.isPresent());
    }

    @Test
    void changePassword() {
        traineeRepository.changePassword("Ali.Valiyev", "333");

        Optional<Trainee> traineeOptional = traineeRepository.findTraineeByUser_UserName("Ali.Valiyev");
        Trainee trainee1 = traineeOptional.get();
        Assertions.assertEquals("333", trainee1.getUser().getPassword());
    }

    @Test
    void activateOrDeactivateTrainee() {
        traineeRepository.activateOrDeactivateTrainee("Ali.Valiyev");
        Optional<Trainee> traineeOptional1 = traineeRepository.findTraineeByUser_UserName("Ali.Valiyev");
        Trainee trainee1 = traineeOptional1.get();

        Assertions.assertFalse(trainee1.getUser().isActive());

        traineeRepository.activateOrDeactivateTrainee("Ali.Valiyev");
        Optional<Trainee> traineeOptional2 = traineeRepository.findTraineeByUser_UserName("Ali.Valiyev");
        Trainee trainee2 = traineeOptional2.get();

        Assertions.assertTrue(trainee2.getUser().isActive());
    }

    @Test
    void deleteByUser_UserName() {
        traineeRepository.deleteByUser_UserName("Ali.Valiyev");

        Optional<Trainee> traineeOptional = traineeRepository.findTraineeByUser_UserName("Ali.Valiyev");
        Assertions.assertFalse(traineeOptional.isPresent());
    }

    @Test
    void findTraineeIdByUsername() {
        Optional<Integer> OptionalId = traineeRepository.findTraineeIdByUsername("Ali.Valiyev");
        Assertions.assertTrue(OptionalId.isPresent());
        Integer id = OptionalId.get();

        Assertions.assertEquals(trainee.getId(), id);
    }

    @Test
    void existsByUser_UserName() {
        boolean result1 = traineeRepository.existsByUser_UserName("Ali.Valiyev");
        boolean result2 = traineeRepository.existsByUser_UserName("Botir.Sobirov");

        Assertions.assertTrue(result1);
        Assertions.assertFalse(result2);
    }
}