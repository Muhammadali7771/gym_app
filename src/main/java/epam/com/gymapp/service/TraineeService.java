package epam.com.gymapp.service;

import epam.com.gymapp.dto.*;
import epam.com.gymapp.dto.trainee.TraineeCreateDto;
import epam.com.gymapp.dto.trainee.TraineeDto;
import epam.com.gymapp.dto.trainee.TraineeUpdateDto;
import epam.com.gymapp.dto.trainer.TrainerShortDto;
import epam.com.gymapp.dto.training.TrainingDto;

import java.util.Date;
import java.util.List;

public interface TraineeService {
    RegistrationResponseDto create(TraineeCreateDto traineeCreateDto);

    TokenResponse login(LoginRequestDto dto);


    TraineeDto getTraineeByUsername(String username);

    void changePassword(ChangeLoginDto dto);

    TraineeDto update(String username, TraineeUpdateDto traineeUpdateDto);

    void activateOrDeactivateTrainee(String username);

    void deleteTraineeByUsername(String username);

    List<TrainingDto> getTraineeTrainingsList(String traineeUsername, Date fromDate, Date toDate, String trainerName, Integer trainingTypeId);

    List<TrainerShortDto> getTrainersListNotAssignedOnTrainee(String traineeUsername);

    List<TrainerShortDto> updateTraineeTrainerList(UpdateTraineeTrainersListDto dto);
}
