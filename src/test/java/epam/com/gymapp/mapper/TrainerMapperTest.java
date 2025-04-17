package epam.com.gymapp.mapper;

import epam.com.gymapp.dto.trainer.TrainerCreateDto;
import epam.com.gymapp.entity.Trainer;
import epam.com.gymapp.entity.TrainingType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    }

    @Test
    void toDto() {
    }

    @Test
    void toShortDto() {
    }

    @Test
    void toShortDtoList() {
    }
}