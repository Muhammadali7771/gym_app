package epam.com.gymapp.dto.trainee;


import com.fasterxml.jackson.annotation.JsonProperty;
import epam.com.gymapp.dto.trainer.TrainerShortDto;

import java.util.Date;
import java.util.List;

public record TraineeDto(String firstName,
                         String lastName,
                         Boolean isActive,
                         Date dateOfBirth,
                         String address,
                         @JsonProperty("trainers")
                         List<TrainerShortDto> trainerDtos
) {
}
