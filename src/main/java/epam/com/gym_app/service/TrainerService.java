package epam.com.gym_app.service;

import epam.com.gym_app.dto.ChangeLoginDto;
import epam.com.gym_app.dto.LoginRequestDto;
import epam.com.gym_app.dto.RegistrationResponseDto;
import epam.com.gym_app.dto.TokenResponse;
import epam.com.gym_app.dto.trainer.TrainerCreateDto;
import epam.com.gym_app.dto.trainer.TrainerDto;
import epam.com.gym_app.dto.trainer.TrainerUpdateDto;
import epam.com.gym_app.dto.training.TrainingDto;

import java.util.Date;
import java.util.List;

public interface TrainerService {
    RegistrationResponseDto create(TrainerCreateDto dto);

    TokenResponse login(LoginRequestDto dto);

    TrainerDto getTrainerByUsername(String username);

    void changePassword(ChangeLoginDto dto);

    TrainerDto update(TrainerUpdateDto dto, String username);

    void activateOrDeactivateTrainer(String username);

    List<TrainingDto> getTrainerTrainingsList(String trainerUsername, Date fromDate, Date toDate, String traineeName);
}
