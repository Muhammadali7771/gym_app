package epam.com.gym_app.dto.trainer;


public record TrainerUpdateDto(String firstName,
                               String lastName,
                               boolean isActive,
                               Integer specializationId) {
}
