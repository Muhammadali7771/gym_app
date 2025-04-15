package epam.com.gymapp.dto.training;

import java.util.Date;

public record TrainingDto(String trainingName,
                          Date trainingDate,
                          Integer trainingTypeId,
                          Number trainingDuration,
                          String traineeUsername,
                          String trainerUsername) {
}
