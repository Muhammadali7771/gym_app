package epam.com.gymapp.dto.trainer;


public record TrainerUpdateDto(String firstName,
                               String lastName,
                               Boolean isActive,
                               Integer specializationId) {
}
