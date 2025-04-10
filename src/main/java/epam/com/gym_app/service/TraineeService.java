package epam.com.gym_app.service;

import epam.com.gym_app.dto.ChangeLoginDto;
import epam.com.gym_app.dto.LoginRequestDto;
import epam.com.gym_app.dto.RegistrationResponseDto;
import epam.com.gym_app.dto.UpdateTraineeTrainersListDto;
import epam.com.gym_app.dto.trainee.TraineeCreateDto;
import epam.com.gym_app.dto.trainee.TraineeDto;
import epam.com.gym_app.dto.trainee.TraineeUpdateDto;
import epam.com.gym_app.dto.trainer.TrainerShortDto;
import epam.com.gym_app.dto.training.TrainingDto;

import java.util.Date;
import java.util.List;

public interface TraineeService {
    RegistrationResponseDto create(TraineeCreateDto traineeCreateDto);

    void login(LoginRequestDto dto);


    TraineeDto getTraineeByUsername(String username);

    void changePassword(ChangeLoginDto dto);

    TraineeDto update(String username, TraineeUpdateDto traineeUpdateDto);

    void activateOrDeactivateTrainee(String username);

    void deleteTraineeByUsername(String username);

    List<TrainingDto> getTraineeTrainingsList(String traineeUsername, Date fromDate, Date toDate, String trainerName, Integer trainingTypeId);

    List<TrainerShortDto> getTrainersListNotAssignedOnTrainee(String traineeUsername);

    List<TrainerShortDto> updateTraineeTrainerList(UpdateTraineeTrainersListDto dto);
}
