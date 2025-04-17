package epam.com.gymapp.mapper;

import epam.com.gymapp.dto.trainee.TraineeShortDto;
import epam.com.gymapp.dto.trainer.TrainerCreateDto;
import epam.com.gymapp.dto.trainer.TrainerDto;
import epam.com.gymapp.dto.trainer.TrainerShortDto;
import epam.com.gymapp.dto.trainer.TrainerUpdateDto;
import epam.com.gymapp.entity.Trainee;
import epam.com.gymapp.entity.Trainer;
import epam.com.gymapp.entity.TrainingType;
import epam.com.gymapp.entity.User;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class TrainerMapperTest {
    @Autowired
    private TrainerMapper trainerMapper;

    @Test
    void toEntity() {
        String firstName = "John";
        String lastName = "Doe";
        Integer specializationId = 2;
        TrainerCreateDto trainerCreateDto = new TrainerCreateDto(firstName, lastName, specializationId);
        TrainingType trainingType = new TrainingType();
        trainingType.setId(2);

        Trainer trainer = trainerMapper.toEntity(trainerCreateDto, trainingType);

        Assertions.assertEquals(firstName, trainer.getUser().getFirstName());
        Assertions.assertEquals(lastName, trainer.getUser().getLastName());
        Assertions.assertEquals(trainingType.getId(), trainer.getSpecialization().getId());
        Assertions.assertTrue(trainer.getUser().getIsActive());
    }

    @Test
    void partialUpdate() {
        String firstName = "ali";
        String lastName = "valiyev";
        TrainerUpdateDto trainerUpdateDto = new TrainerUpdateDto(firstName, lastName, true, 1);
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1);

        Trainer trainer = new Trainer();
        User user = new User();
        user.setFirstName("aaa");
        user.setLastName("bbb");
        user.setIsActive(true);
        TrainingType specialization = new TrainingType();
        specialization.setId(2);
        trainer.setSpecialization(specialization);
        trainer.setUser(user);

        Trainer result = trainerMapper.partialUpdate(trainerUpdateDto, trainingType, trainer);

        Assertions.assertEquals(firstName, result.getUser().getFirstName());
        Assertions.assertEquals(lastName, result.getUser().getLastName());
        Assertions.assertEquals(1, trainer.getSpecialization().getId());
        Assertions.assertEquals(true, trainer.getUser().getIsActive());
    }

    @Test
    void toDto() {
        String firstName = "ali";
        String lastName = "valiyev";
        TrainingType trainingType = new TrainingType();
        trainingType.setId(2);
        Trainer trainer = new Trainer();
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setIsActive(true);
        trainer.setUser(user);
        trainer.setSpecialization(trainingType);


        Trainee trainee = new Trainee();
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setUserName("John.Doe");
        user1.setIsActive(false);
        trainee.setUser(user1);
        trainer.setTrainees(List.of(trainee));

        TrainerDto dto = trainerMapper.toDto(trainer);

        Assertions.assertEquals(firstName, dto.firstName());
        Assertions.assertEquals(lastName, dto.lastName());
        Assertions.assertEquals(true, dto.isActive());
        Assertions.assertEquals(2, dto.specializationId());
        Assertions.assertEquals(1, dto.traineeShortDtos().size());

        TraineeShortDto traineeShortDto = dto.traineeShortDtos().get(0);

        Assertions.assertEquals("John" , traineeShortDto.firstName());
        Assertions.assertEquals("Doe", traineeShortDto.lastName());
        Assertions.assertEquals("John.Doe", traineeShortDto.username());
        Assertions.assertFalse(traineeShortDto.isActive());
    }

    @Test
    void toDto_2(){
        Trainer trainer = null;
        TrainerDto dto = trainerMapper.toDto(trainer);
        Assertions.assertNull(dto);
    }

    @Test
    void toShortDto() {
        String firstName = "John";
        String lastName = "Doe";
        String username = "John.Doe";

        Trainer trainer = new Trainer();
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUserName(username);
        user.setIsActive(true);
        trainer.setUser(user);
        TrainingType trainingType = new TrainingType();
        trainingType.setId(2);
        trainer.setSpecialization(trainingType);

        TrainerShortDto dto = trainerMapper.toShortDto(trainer);

        Assertions.assertEquals(firstName, dto.firstName());
        Assertions.assertEquals(lastName, dto.lastName());
        Assertions.assertEquals(username, dto.username());
        Assertions.assertTrue(dto.isActive());
        Assertions.assertEquals(2, dto.specializationId());
    }

    @Test
    void toShortDto_2(){
        Trainer trainer = null;
        TrainerShortDto dto = trainerMapper.toShortDto(trainer);
        Assertions.assertNull(dto);
    }

    @Test
    void toShortDtoList() {
        Trainer trainer1 = new Trainer();
        User user1 = new User();
        user1.setFirstName("ali");
        user1.setLastName("valiyev");
        user1.setUserName("ali.valiyev");
        user1.setIsActive(true);
        trainer1.setUser(user1);
        TrainingType trainingType1 = new TrainingType();
        trainingType1.setId(2);
        trainer1.setSpecialization(trainingType1);

        Trainer trainer2 = new Trainer();
        User user2 = new User();
        user2.setUserName("John.Doe");
        user2.setFirstName("John");
        user2.setLastName("Doe");
        user2.setIsActive(false);
        trainer2.setUser(user2);
        TrainingType trainingType = new TrainingType();
        trainingType.setId(3);
        trainer2.setSpecialization(trainingType);

        List<Trainer> trainers = List.of(trainer1, trainer2);
        List<TrainerShortDto> trainerShortDtos = trainerMapper.toShortDtoList(trainers);

        Assertions.assertEquals(2, trainerShortDtos.size());
        TrainerShortDto dto1 = trainerShortDtos.get(0);
        TrainerShortDto dto2 = trainerShortDtos.get(1);
        Assertions.assertNotNull(dto1);
        Assertions.assertNotNull(dto2);
        Assertions.assertEquals(trainer1.getUser().getFirstName(),dto1.firstName());
        Assertions.assertEquals(trainer1.getUser().getLastName(), dto1.lastName());
        Assertions.assertEquals(trainer1.getUser().getUserName(), dto1.username());
        Assertions.assertEquals(trainer1.getUser().getIsActive(), dto1.isActive());
        Assertions.assertEquals(trainer1.getSpecialization().getId() , dto1.specializationId());

        Assertions.assertEquals(trainer2.getUser().getFirstName(),dto2.firstName());
        Assertions.assertEquals(trainer2.getUser().getLastName(), dto2.lastName());
        Assertions.assertEquals(trainer2.getUser().getUserName(), dto2.username());
        Assertions.assertEquals(trainer2.getUser().getIsActive(), dto2.isActive());
        Assertions.assertEquals(trainer2.getSpecialization().getId() , dto2.specializationId());
    }
}