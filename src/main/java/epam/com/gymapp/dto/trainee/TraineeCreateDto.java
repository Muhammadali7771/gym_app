package epam.com.gymapp.dto.trainee;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.util.Date;

public record TraineeCreateDto(
        @NotBlank(message = "first name can not be null and blank")
        String firstName,
        @NotBlank(message = "last name can not be null and blank")
        String lastName,
        @Past(message = "birthday should be in the past")
        Date dateOfBirth,
        String address) {
}
