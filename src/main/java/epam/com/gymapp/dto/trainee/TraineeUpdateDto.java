package epam.com.gymapp.dto.trainee;


import java.util.Date;


public record TraineeUpdateDto(String firstName,
                               String lastName,
                               Boolean isActive,
                               Date dateOfBirth,
                               String address) {
}

