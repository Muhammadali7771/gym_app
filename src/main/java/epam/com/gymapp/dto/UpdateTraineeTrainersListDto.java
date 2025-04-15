package epam.com.gymapp.dto;

import java.util.List;

public record UpdateTraineeTrainersListDto(String traineeUsername,
                                              List<String> trainers) {
}
