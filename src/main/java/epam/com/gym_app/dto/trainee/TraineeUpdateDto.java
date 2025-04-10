package epam.com.gym_app.dto.trainee;


import java.util.Date;


public record TraineeUpdateDto(String firstName,
                               String lastName,
                               boolean isActive,
                               Date dateOfBirth,
                               String address) {
}

