package epam.com.gymapp.dto.training;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record TrainingCreateDto(
        @NotBlank(message = "trainee username can not be null and blank")
        String traineeUsername,
        @NotBlank(message = "trainer username can not be null and blank")
        String trainerUsername,
        @NotBlank(message = "training name can not be null and blank")
        String trainingName,
        @NotNull(message = "training date can not be null")
        Date trainingDate,
        @NotNull(message = "training type id can not be null")
        Integer trainingTypeId,
        @NotNull(message = "training duration can not be null")
        Number trainingDuration) {
}
