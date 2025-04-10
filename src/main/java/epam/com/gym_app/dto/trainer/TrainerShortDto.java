package epam.com.gym_app.dto.trainer;


public record TrainerShortDto(String username,
                              String firstName,
                              String lastName,
                              boolean isActive,
                              Integer specializationId) {
}
