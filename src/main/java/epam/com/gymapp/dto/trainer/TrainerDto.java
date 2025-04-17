package epam.com.gymapp.dto.trainer;


import com.fasterxml.jackson.annotation.JsonProperty;
import epam.com.gymapp.dto.trainee.TraineeShortDto;

import java.util.List;

public record TrainerDto(String firstName,
                         String lastName,
                         Boolean isActive,
                         Integer specializationId,
                         @JsonProperty("trainee_list")
                         List<TraineeShortDto> traineeShortDtos) {
}
