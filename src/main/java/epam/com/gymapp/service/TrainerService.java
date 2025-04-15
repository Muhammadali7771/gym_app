package epam.com.gymapp.service;

import epam.com.gymapp.dto.ChangeLoginDto;
import epam.com.gymapp.dto.LoginRequestDto;
import epam.com.gymapp.dto.RegistrationResponseDto;
import epam.com.gymapp.dto.TokenResponse;
import epam.com.gymapp.dto.trainer.TrainerCreateDto;
import epam.com.gymapp.dto.trainer.TrainerDto;
import epam.com.gymapp.dto.trainer.TrainerUpdateDto;
import epam.com.gymapp.dto.training.TrainingDto;

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
