package epam.com.gym_app.dto.trainer;


import com.fasterxml.jackson.annotation.JsonProperty;
import epam.com.gym_app.dto.trainee.TraineeShortDto;

import java.util.List;

public record TrainerDto(String firstName,
                         String lastName,
                         boolean isActive,
                         Integer specializationId,
                         @JsonProperty("trainee_list")
                         List<TraineeShortDto> traineeShortDtos) {
}
