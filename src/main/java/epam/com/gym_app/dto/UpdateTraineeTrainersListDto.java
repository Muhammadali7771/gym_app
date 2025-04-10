package epam.com.gym_app.dto;

import java.util.List;

public record UpdateTraineeTrainersListDto(String traineeUsername,
                                              List<String> trainers) {
}
