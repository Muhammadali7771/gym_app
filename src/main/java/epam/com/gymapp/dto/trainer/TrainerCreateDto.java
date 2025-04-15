package epam.com.gymapp.dto.trainer;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TrainerCreateDto(
        @NotBlank(message = "first name can not be blank and null")
        String firstName,
        @NotBlank(message = "last name can not be blank and null")
        String lastName,
        @NotNull(message = "specializationId can not be null")
        Integer specializationId) {
}
