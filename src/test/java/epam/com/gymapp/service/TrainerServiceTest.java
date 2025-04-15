package epam.com.gymapp.service;

import epam.com.gymapp.dto.ChangeLoginDto;
import epam.com.gymapp.dto.LoginRequestDto;
import epam.com.gymapp.dto.RegistrationResponseDto;
import epam.com.gymapp.dto.trainer.TrainerCreateDto;
import epam.com.gymapp.dto.trainer.TrainerDto;
import epam.com.gymapp.dto.trainer.TrainerUpdateDto;
import epam.com.gymapp.entity.Trainer;
import epam.com.gymapp.entity.Training;
import epam.com.gymapp.entity.User;
import epam.com.gymapp.exception.AuthenticationException;
import epam.com.gymapp.exception.ResourceNotFoundException;
import epam.com.gymapp.mapper.TrainerMapper;
import epam.com.gymapp.mapper.TrainingMapper;
import epam.com.gymapp.repository.TrainerRepository;
import epam.com.gymapp.repository.TrainingRepository;
import epam.com.gymapp.service.impl.TrainerServiceImpl;
import epam.com.gymapp.util.UsernamePasswordGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {
    @InjectMocks
    private TrainerServiceImpl trainerService;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TrainerMapper trainerMapper;
    @Mock
    private UsernamePasswordGenerator usernamePasswordGenerator;
    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private TrainingMapper trainingMapper;

    @Test
    void create() {
        TrainerCreateDto dto = new TrainerCreateDto("Botir", "Sobirov", 1);
        Trainer trainer = new Trainer();
        User user = new User();
        trainer.setUser(user);
        Mockito.when(trainerMapper.toEntity(dto)).thenReturn(trainer);
        Mockito.when(usernamePasswordGenerator.generateUsername(dto.firstName(), dto.lastName()))
                .thenReturn("Botir.Sobirov");
        Mockito.when(usernamePasswordGenerator.generatePassword()).thenReturn("5555555555");
        Mockito.when(trainerRepository.save(trainer)).thenReturn(trainer);

        RegistrationResponseDto responseDto = trainerService.create(dto);

        Mockito.verify(trainerMapper).toEntity(dto);
        Mockito.verify(usernamePasswordGenerator).generateUsername(dto.firstName(), dto.lastName());
        Mockito.verify(usernamePasswordGenerator).generatePassword();
        Mockito.verify(trainerRepository).save(trainer);
        Assertions.assertEquals("Botir.Sobirov", responseDto.username());
        Assertions.assertEquals("5555555555", responseDto.password());
    }

    @Test
    void login_Success() {
        LoginRequestDto dto = new LoginRequestDto("Botir.Sobirov", "1234567");
        Mockito.when(trainerRepository.checkUsernameAndPasswordMatch(dto.username(), dto.password()))
                .thenReturn(true);

        trainerService.login(dto);

        Mockito.verify(trainerRepository).checkUsernameAndPasswordMatch(dto.username(), dto.password());
    }

    @Test
    void login_throwsException() {
        LoginRequestDto dto = new LoginRequestDto("Botir.Sobirov", "1234567");
        Mockito.when(trainerRepository.checkUsernameAndPasswordMatch(dto.username(), dto.password()))
                .thenReturn(false);

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> trainerService.login(dto));

        Assertions.assertEquals("username or password is incorrect!", exception.getMessage());
        Mockito.verify(trainerRepository).checkUsernameAndPasswordMatch(dto.username(), dto.password());
    }

    @Test
    void getTrainerByUsername_Success() {
        String username = "John.Doe";
        Trainer trainer = new Trainer();
        Mockito.when(trainerRepository.findTrainerByUser_UserName(username))
                .thenReturn(Optional.of(trainer));
        Mockito.when(trainerMapper.toDto(trainer)).thenReturn(new TrainerDto("John", "Doe",
                true, null, null));

        trainerService.getTrainerByUsername(username);

        Mockito.verify(trainerRepository).findTrainerByUser_UserName(username);
        Mockito.verify(trainerMapper).toDto(trainer);
    }

    @Test
    void getTrainerByUsername_ThrowsException() {
        String username = "John.Doe";
        Mockito.when(trainerRepository.findTrainerByUser_UserName(username))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> trainerService.getTrainerByUsername(username));
        Assertions.assertEquals("Trainer not found", exception.getMessage());

        Mockito.verify(trainerRepository).findTrainerByUser_UserName(username);
        Mockito.verify(trainerMapper, Mockito.never()).toDto(Mockito.any());
    }

    @Test
    void changePassword_Success() {
        ChangeLoginDto dto = new ChangeLoginDto("John.Doe", "123", "777");
        Mockito.when(trainerRepository.checkUsernameAndPasswordMatch(dto.username(), dto.oldPassword()))
                .thenReturn(true);
        Mockito.doNothing().when(trainerRepository).changePassword(dto.username(), dto.newPassword());

        trainerService.changePassword(dto);

        Mockito.verify(trainerRepository).checkUsernameAndPasswordMatch(dto.username(), dto.oldPassword());
        Mockito.verify(trainerRepository).changePassword(dto.username(), dto.newPassword());
    }

    @Test
    void changePassword_ThrowsException() {
        ChangeLoginDto dto = new ChangeLoginDto("John.Doe", "123", "777");
        Mockito.when(trainerRepository.checkUsernameAndPasswordMatch(dto.username(), dto.oldPassword()))
                .thenReturn(false);

        Assertions.assertThrows(AuthenticationException.class, () -> trainerService.changePassword(dto));
        Mockito.verify(trainerRepository).checkUsernameAndPasswordMatch(dto.username(), dto.oldPassword());
        Mockito.verify(trainerRepository, Mockito.never()).changePassword(dto.username(), dto.newPassword());
    }

    @Test
    void update_Success() {
        TrainerUpdateDto dto = new TrainerUpdateDto("JOHN", "DOE", true, 3);
        String username = "John.Doe";
        Trainer trainer = new Trainer();
        Trainer trainer1 = new Trainer();
        Trainer updatedTrainer = new Trainer();
        Mockito.when(trainerRepository.findTrainerByUser_UserName(username))
                .thenReturn(Optional.of(trainer));
        Mockito.when(trainerMapper.partialUpdate(dto, trainer))
                .thenReturn(trainer1);
        Mockito.when(trainerRepository.save(trainer1))
                .thenReturn(updatedTrainer);
        Mockito.when(trainerMapper.toDto(updatedTrainer))
                .thenReturn(new TrainerDto("JOHN", "DOE", true, 3, null));

        trainerService.update(dto, username);

        Mockito.verify(trainerRepository).findTrainerByUser_UserName(username);
        Mockito.verify(trainerMapper).partialUpdate(dto, trainer);
        Mockito.verify(trainerRepository).save(trainer1);
        Mockito.verify(trainerMapper).toDto(updatedTrainer);
    }

    @Test
    void update_throwsException() {
        TrainerUpdateDto dto = new TrainerUpdateDto("JOHN", "DOE", true, 3);
        String username = "John.Doe";
        Mockito.when(trainerRepository.findTrainerByUser_UserName(username))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> trainerService.update(dto, username));

        Assertions.assertEquals("Trainer not found", exception.getMessage());
        Mockito.verify(trainerRepository).findTrainerByUser_UserName(username);
        Mockito.verify(trainerMapper, Mockito.never()).partialUpdate(Mockito.any(), Mockito.any());
        Mockito.verify(trainerRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(trainerMapper, Mockito.never()).toDto(Mockito.any());
    }


    @Test
    void activateOrDeactivateTrainer_Success() {
        String username = "John.Doe";
        Mockito.when(trainerRepository.existsByUser_UserName(username))
                .thenReturn(true);
        Mockito.doNothing().when(trainerRepository).activateOrDeactivateTrainer(username);

        trainerService.activateOrDeactivateTrainer(username);

        Mockito.verify(trainerRepository).existsByUser_UserName(username);
        Mockito.verify(trainerRepository).activateOrDeactivateTrainer(username);
    }

    @Test
    void activateOrDeactivateTrainer_ThrowsException() {
        String username = "John.Doe";
        Mockito.when(trainerRepository.existsByUser_UserName(username))
                .thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> trainerService.activateOrDeactivateTrainer(username));

        Assertions.assertEquals("Trainer not found", exception.getMessage());
        Mockito.verify(trainerRepository).existsByUser_UserName(username);
        Mockito.verify(trainerRepository, Mockito.never()).activateOrDeactivateTrainer(username);
    }

    @Test
    void getTrainerTrainingsList_Success() {
        String trainerUsername = "John.Doe";
        Date fromDate = new Date();
        Date toDate = new Date();
        String traineeName = "Ali";

        Mockito.when(trainerRepository.existsByUser_UserName(trainerUsername))
                .thenReturn(true);
        ArrayList<Training> trainings = new ArrayList<>();
        Mockito.when(trainingRepository.getTrainerTrainingsListByTrainerUsernameAndCriteria(trainerUsername, fromDate,
                toDate, traineeName)).thenReturn(trainings);
        Mockito.when(trainingMapper.toDtoList(trainings)).thenReturn(new ArrayList<>());

        trainerService.getTrainerTrainingsList(trainerUsername, fromDate, toDate, traineeName);

        Mockito.verify(trainerRepository).existsByUser_UserName(trainerUsername);
        Mockito.verify(trainingRepository).getTrainerTrainingsListByTrainerUsernameAndCriteria(trainerUsername, fromDate,
                toDate, traineeName);
        Mockito.verify(trainingMapper).toDtoList(Mockito.any());
    }

    @Test
    void getTrainerTrainingsList_ThrowsException(){
        String trainerUsername = "John.Doe";
        Date fromDate = new Date();
        Date toDate = new Date();
        String traineeName = "Ali";
        Mockito.when(trainerRepository.existsByUser_UserName(trainerUsername))
                .thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                trainerService.getTrainerTrainingsList(trainerUsername, fromDate, toDate, traineeName));
        Assertions.assertEquals("Trainer not found", exception.getMessage());
        Mockito.verify(trainingRepository, Mockito.never()).getTrainerTrainingsListByTrainerUsernameAndCriteria(trainerUsername, fromDate, toDate, traineeName);
        Mockito.verify(trainingMapper, Mockito.never()).toDtoList(Mockito.any());
    }
}