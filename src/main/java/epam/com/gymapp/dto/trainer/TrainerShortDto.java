package epam.com.gymapp.dto.trainer;


public record TrainerShortDto(String username,
                              String firstName,
                              String lastName,
                              Boolean isActive,
                              Integer specializationId) {
}
