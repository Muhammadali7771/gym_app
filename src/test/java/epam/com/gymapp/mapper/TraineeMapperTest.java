package epam.com.gymapp.mapper;

import epam.com.gymapp.dto.trainee.TraineeCreateDto;
import epam.com.gymapp.dto.trainee.TraineeDto;
import epam.com.gymapp.dto.trainee.TraineeUpdateDto;
import epam.com.gymapp.dto.trainer.TrainerShortDto;
import epam.com.gymapp.entity.Trainee;
import epam.com.gymapp.entity.Trainer;
import epam.com.gymapp.entity.TrainingType;
import epam.com.gymapp.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TraineeMapperTest {
    @Autowired
    private TraineeMapper traineeMapper;

    @Test
    void toEntity() {
        String fistName = "ali";
        String lastName = "valiyev";
        Date date = new Date();
        String address = "Tashkent";
        TraineeCreateDto dto = new TraineeCreateDto(fistName, lastName, date, address);
        Trainee trainee = traineeMapper.toEntity(dto);
        User user = trainee.getUser();

        Assertions.assertEquals(fistName, user.getFirstName());
        Assertions.assertEquals(lastName, user.getLastName());
        Assertions.assertTrue(user.getIsActive());
        Assertions.assertEquals(date, trainee.getDateOfBirth());
        Assertions.assertEquals(address, trainee.getAddress());
    }

    @Test
    void toEntity2() {
        Trainee trainee = traineeMapper.toEntity(null);
        Assertions.assertNull(trainee);
    }

    @Test
    void partialUpdate() {
        String firstName = "ALI";
        String lastName = "VALIYEV";
        Boolean isActive = true;
        Date date = new Date(2000, 4, 4);
        String address = "TASHKENT";
        TraineeUpdateDto dto = new TraineeUpdateDto(firstName, lastName, isActive, date, address);
        Trainee trainee = new Trainee();
        trainee.setUser(new User());

        traineeMapper.partialUpdate(dto, trainee);

        Assertions.assertEquals(firstName, trainee.getUser().getFirstName());
        Assertions.assertEquals(lastName, trainee.getUser().getLastName());
        Assertions.assertTrue(isActive);
        Assertions.assertEquals(date, trainee.getDateOfBirth());
        Assertions.assertEquals(address, trainee.getAddress());
    }

    @Test
    void partialUpdate_2() {
        TraineeUpdateDto traineeUpdateDto = new TraineeUpdateDto("ALI", "VALIYEV",
                null, null, "TASHKENT");
        Trainee existingTrainee = new Trainee();
        existingTrainee.setDateOfBirth(new Date(2000, 4, 4));
        User user = new User();
        user.setFirstName("ali");
        user.setLastName("valiyev");
        existingTrainee.setUser(user);

        traineeMapper.partialUpdate(traineeUpdateDto, existingTrainee);

        Assertions.assertEquals("ALI", existingTrainee.getUser().getFirstName());
        Assertions.assertEquals("VALIYEV", existingTrainee.getUser().getLastName());
        Assertions.assertEquals("TASHKENT", existingTrainee.getAddress());
        Assertions.assertEquals(new Date(2000, 4, 4), existingTrainee.getDateOfBirth());
    }

    @Test
    void toDto() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setFirstName("ali");
        user.setLastName("valiyev");
        user.setIsActive(false);
        trainee.setUser(user);
        trainee.setDateOfBirth(new Date(2000, 4, 4));
        trainee.setAddress("TASHKENT");
        Trainer trainer1 = new Trainer();
        User user1 = new User();
        user1.setFirstName("JOHN");
        user1.setLastName("Doe");
        user1.setIsActive(true);
        trainer1.setUser(user1);
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1);
        trainer1.setSpecialization(trainingType);
        trainee.setTrainers(List.of(trainer1));

        TraineeDto dto = traineeMapper.toDto(trainee);

        Assertions.assertEquals("ali", dto.firstName());
        Assertions.assertEquals("valiyev", dto.lastName());
        Assertions.assertFalse(dto.isActive());
        Assertions.assertEquals("TASHKENT", dto.address());
        Assertions.assertEquals(new Date(2000, 4, 4), trainee.getDateOfBirth());
        Assertions.assertEquals(1, dto.trainerDtos().size());
        TrainerShortDto trainerShortDto = dto.trainerDtos().get(0);
        Assertions.assertEquals("JOHN", trainerShortDto.firstName());
        Assertions.assertEquals("Doe", trainerShortDto.lastName());
        Assertions.assertTrue(trainerShortDto.isActive());
        Assertions.assertEquals(1, trainerShortDto.specializationId());
    }

    @Test
    void toDto_2() {
        TraineeDto dto = traineeMapper.toDto(null);
        Assertions.assertNull(dto);
    }
}