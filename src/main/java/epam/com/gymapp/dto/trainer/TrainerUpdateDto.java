package epam.com.gymapp.dto.trainer;


public record TrainerUpdateDto(String firstName,
                               String lastName,
                               boolean isActive,
                               Integer specializationId) {
}
