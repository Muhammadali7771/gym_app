package epam.com.gymapp.service.impl;


import epam.com.gymapp.config.security.JwtTokenService;
import epam.com.gymapp.dto.ChangeLoginDto;
import epam.com.gymapp.dto.LoginRequestDto;
import epam.com.gymapp.dto.RegistrationResponseDto;
import epam.com.gymapp.dto.TokenResponse;
import epam.com.gymapp.dto.trainer.TrainerCreateDto;
import epam.com.gymapp.dto.trainer.TrainerDto;
import epam.com.gymapp.dto.trainer.TrainerUpdateDto;
import epam.com.gymapp.dto.training.TrainingDto;
import epam.com.gymapp.entity.Trainer;
import epam.com.gymapp.entity.Training;
import epam.com.gymapp.entity.User;
import epam.com.gymapp.exception.ResourceNotFoundException;
import epam.com.gymapp.mapper.TrainerMapper;
import epam.com.gymapp.mapper.TrainingMapper;
import epam.com.gymapp.repository.TrainerRepository;
import epam.com.gymapp.repository.TrainingRepository;
import epam.com.gymapp.service.TrainerService;
import epam.com.gymapp.util.UsernamePasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;
    private final UsernamePasswordGenerator usernamePasswordGenerator;
    private final TrainingRepository trainingRepository;
    private final TrainingMapper trainingMapper;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public RegistrationResponseDto create(TrainerCreateDto dto){
        Trainer trainer = trainerMapper.toEntity(dto);
        String username = usernamePasswordGenerator
                .generateUsername(dto.firstName(), dto.lastName());
        String password = usernamePasswordGenerator.generatePassword();
        User user = trainer.getUser();
        user.setUserName(username);
        user.setPassword(passwordEncoder.encode(password));
        trainerRepository.save(trainer);
        return new RegistrationResponseDto(username, password);
    }

    @Override
    public TokenResponse login(LoginRequestDto dto){
        String password = dto.password();
        String username = dto.username();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        authenticationManager.authenticate(authenticationToken);
        String token = jwtTokenService.generateToken(username);
        return new TokenResponse(token);
    }

    @Override
    public TrainerDto getTrainerByUsername(String username) {
        Trainer trainer = trainerRepository.findTrainerByUser_UserName(username)
                .orElseThrow(() -> {
                    log.warn("Trainee not found with username : {}", username);
                    return new ResourceNotFoundException("Trainer not found");});
        return trainerMapper.toDto(trainer);
    }

    @Override
    public void changePassword(ChangeLoginDto dto) {
        /*if (!trainerRepository.checkUsernameAndPasswordMatch(dto.username(), dto.oldPassword())) {
            log.warn("Login failed !");
            throw new AuthenticationException("username or password is incorrect");
        }
        trainerRepository.changePassword(dto.username(), dto.newPassword());*/
        String username = dto.username();
        String oldPassword = dto.oldPassword();
        String newPassword = dto.newPassword();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, oldPassword);
        authenticationManager.authenticate(authenticationToken);
        trainerRepository.changePassword(dto.username(), passwordEncoder.encode(newPassword));
    }

    @Override
    public TrainerDto update(TrainerUpdateDto dto, String username){
        Trainer trainer = trainerRepository.findTrainerByUser_UserName(username)
                .orElseThrow(() ->{
                    log.warn("Trainer not found with username : {}", username);
                    return new ResourceNotFoundException("Trainer not found");
                });
        Trainer trainer1 = trainerMapper.partialUpdate(dto, trainer);
        Trainer updatedTrainer = trainerRepository.save(trainer1);
        return trainerMapper.toDto(updatedTrainer);
    }

    @Override
    public void activateOrDeactivateTrainer(String username){
        if (!trainerRepository.existsByUser_UserName(username)) {
            log.warn("Trainer not found with username : {}", username);
            throw new ResourceNotFoundException("Trainer not found");
        }
        trainerRepository.activateOrDeactivateTrainer(username);
    }

    @Override
    public List<TrainingDto> getTrainerTrainingsList(String trainerUsername, Date fromDate, Date toDate, String traineeName) {
        if (!trainerRepository.existsByUser_UserName(trainerUsername)) {
            log.warn("Trainer not found with username : {}", trainerUsername);
            throw new ResourceNotFoundException("Trainer not found");
        }
        List<Training> trainings = trainingRepository.getTrainerTrainingsListByTrainerUsernameAndCriteria(trainerUsername, fromDate, toDate, traineeName);
        return trainingMapper.toDtoList(trainings);
    }

}
