package epam.com.gymapp.service;

import epam.com.gymapp.config.security.JwtTokenService;
import epam.com.gymapp.dto.ChangeLoginDto;
import epam.com.gymapp.dto.LoginRequestDto;
import epam.com.gymapp.dto.RegistrationResponseDto;
import epam.com.gymapp.dto.TokenResponse;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @Mock
    private JwtTokenService jwtTokenService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void create() {
        TrainerCreateDto dto = new TrainerCreateDto("Botir", "Sobirov", 1);
        Trainer trainer = new Trainer();
        User user = new User();
        trainer.setUser(user);
        String encodedPassword = "werwrffver123fd";
        String generatedPassword = "5555555555";
        String generatedUsername = "Botir.Sobirov";

        Mockito.when(trainerMapper.toEntity(dto)).thenReturn(trainer);
        Mockito.when(usernamePasswordGenerator.generateUsername(dto.firstName(), dto.lastName()))
                .thenReturn(generatedUsername);
        Mockito.when(usernamePasswordGenerator.generatePassword()).thenReturn(generatedPassword);
        Mockito.when(passwordEncoder.encode(generatedPassword)).thenReturn(encodedPassword);
        Mockito.when(trainerRepository.save(trainer)).thenReturn(trainer);

        RegistrationResponseDto responseDto = trainerService.create(dto);

        Mockito.verify(trainerMapper).toEntity(dto);
        Mockito.verify(usernamePasswordGenerator).generateUsername(dto.firstName(), dto.lastName());
        Mockito.verify(usernamePasswordGenerator).generatePassword();
        Mockito.verify(trainerRepository).save(trainer);
        Assertions.assertEquals(generatedUsername, responseDto.username());
        Assertions.assertEquals(generatedPassword, responseDto.password());
        Assertions.assertEquals(generatedUsername, trainer.getUser().getUserName());
        Assertions.assertEquals(encodedPassword, trainer.getUser().getPassword());
    }

    @Test
    void login_Success() {
        String username = "Botir.Sobirov";
        String password = "1234567";
        String token = "sdfsdfsdfwerwe345dgfgdfgdfg";
        LoginRequestDto dto = new LoginRequestDto(username, password);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(authenticationManager.authenticate(usernamePasswordAuthenticationToken)).thenReturn(authentication);
        Mockito.when(jwtTokenService.generateToken(username)).thenReturn(token);

        TokenResponse tokenResponse = trainerService.login(dto);

        Mockito.verify(authenticationManager).authenticate(usernamePasswordAuthenticationToken);
        Mockito.verify(jwtTokenService).generateToken(username);
        Assertions.assertEquals(token, tokenResponse.token());
    }

    @Test
    void login_throwsException() {
        String username = "Botir.Sobirov";
        String password = "1234567";
        LoginRequestDto dto = new LoginRequestDto(username, password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Mockito.when(authenticationManager.authenticate(authenticationToken)).thenThrow(BadCredentialsException.class);

        Assertions.assertThrows(BadCredentialsException.class ,() -> trainerService.login(dto));

        Mockito.verify(jwtTokenService, Mockito.never()).generateToken(Mockito.anyString());
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
        String username = "John.Doe";
        String oldPassword = "123";
        String newPassword = "777";
        String encodedPassword = "swerwr2342342";
        ChangeLoginDto dto = new ChangeLoginDto(username, oldPassword, newPassword);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, oldPassword);
        Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(authenticationManager.authenticate(usernamePasswordAuthenticationToken)).thenReturn(authentication);
        Mockito.when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        Mockito.doNothing().when(trainerRepository).changePassword(username, encodedPassword);

        trainerService.changePassword(dto);

        Mockito.verify(authenticationManager).authenticate(usernamePasswordAuthenticationToken);
        Mockito.verify(passwordEncoder).encode(newPassword);
        Mockito.verify(trainerRepository).changePassword(username, encodedPassword);
    }

    @Test
    void changePassword_ThrowsException() {
        String username = "John.Doe";
        String oldPassword = "123";
        String newPassword = "777";
        ChangeLoginDto dto = new ChangeLoginDto(username, oldPassword, newPassword);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, oldPassword);

        Mockito.when(authenticationManager.authenticate(usernamePasswordAuthenticationToken))
                .thenThrow(BadCredentialsException.class);

        Assertions.assertThrows(BadCredentialsException.class,() -> trainerService.changePassword(dto));

        Mockito.verify(passwordEncoder, Mockito.never()).encode(newPassword);
        Mockito.verify(trainerRepository, Mockito.never()).changePassword(username, newPassword);
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