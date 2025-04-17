package epam.com.gymapp.service;

import epam.com.gymapp.config.security.JwtTokenService;
import epam.com.gymapp.dto.*;
import epam.com.gymapp.dto.trainee.TraineeCreateDto;
import epam.com.gymapp.dto.trainee.TraineeDto;
import epam.com.gymapp.dto.trainee.TraineeUpdateDto;
import epam.com.gymapp.entity.Trainee;
import epam.com.gymapp.entity.Trainer;
import epam.com.gymapp.entity.Training;
import epam.com.gymapp.entity.User;
import epam.com.gymapp.exception.ResourceNotFoundException;
import epam.com.gymapp.mapper.TraineeMapper;
import epam.com.gymapp.mapper.TrainerMapper;
import epam.com.gymapp.mapper.TrainingMapper;
import epam.com.gymapp.repository.TraineeRepository;
import epam.com.gymapp.repository.TrainerRepository;
import epam.com.gymapp.repository.TrainingRepository;
import epam.com.gymapp.service.impl.TraineeServiceImpl;
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
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {
    @InjectMocks
    private TraineeServiceImpl traineeService;
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TraineeMapper traineeMapper;
    @Mock
    private TrainerMapper trainerMapper;
    @Mock
    private UsernamePasswordGenerator usernamePasswordGenerator;
    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private TrainingMapper trainingMapper;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenService jwtTokenService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void create() {
        TraineeCreateDto traineeCreateDto = new TraineeCreateDto("Ali", "Valiyev",
                new Date(2025, 2, 2), "Tashkent");
        Trainee trainee = new Trainee();
        User user = new User();
        trainee.setUser(user);
        Mockito.when(traineeMapper.toEntity(traineeCreateDto)).thenReturn(trainee);
        Mockito.when(usernamePasswordGenerator.generateUsername(traineeCreateDto.firstName(),
                traineeCreateDto.lastName())).thenReturn("Ali.Valiyev");
        Mockito.when(usernamePasswordGenerator.generatePassword()).thenReturn("2222222222");
        Mockito.when(passwordEncoder.encode("2222222222"))
                .thenReturn("asddfgerwrweeggrerttegegergerwrwerwt");
        Mockito.when(traineeRepository.save(trainee)).thenReturn(trainee);

        RegistrationResponseDto responseDto = traineeService.create(traineeCreateDto);

        Mockito.verify(traineeMapper).toEntity(traineeCreateDto);
        Mockito.verify(usernamePasswordGenerator).generateUsername(traineeCreateDto.firstName(), traineeCreateDto.lastName());
        Mockito.verify(usernamePasswordGenerator).generatePassword();
        Mockito.verify(traineeRepository).save(trainee);
        Assertions.assertEquals("Ali.Valiyev" ,responseDto.username());
        Assertions.assertEquals("2222222222", responseDto.password());
    }

    @Test
    void login_Failure() {
        String username = "Ali.Valiyev";
        String password = "777";
        LoginRequestDto loginRequestDto = new LoginRequestDto(username, password);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Mockito.when(authenticationManager.authenticate(usernamePasswordAuthenticationToken)).thenThrow(BadCredentialsException.class);

        Assertions.assertThrows(BadCredentialsException.class,  () -> traineeService.login(loginRequestDto));
        Mockito.verify(jwtTokenService, Mockito.never()).generateToken(username);
    }

    @Test
    void login_Success(){
        String username = "Ali.Valiyev";
        String password = "777";
        String generatedToken = "dfgergeregergertertertewr";
        Authentication authentication = Mockito.mock(Authentication.class);
        LoginRequestDto loginRequestDto = new LoginRequestDto(username, password);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequestDto.username(), loginRequestDto.password());
        Mockito.when(authenticationManager.authenticate(token)).thenReturn(authentication);
        Mockito.when(jwtTokenService.generateToken(username)).thenReturn(generatedToken);

        TokenResponse tokenResponse = traineeService.login(loginRequestDto);

        Mockito.verify(authenticationManager).authenticate(token);
        Mockito.verify(jwtTokenService).generateToken(username);
        Assertions.assertEquals(generatedToken, tokenResponse.token());
    }

    @Test
    void getTraineeByUsername_Success() {
        String username = "Ali.Valiyev";
        Trainee trainee = new Trainee();
        User user = new User();
        user.setUserName(username);
        trainee.setUser(user);
        trainee.setId(1);

        TraineeDto traineeDto = new TraineeDto("Ali", "Valiyev", true, new Date(), "Navai", null);

        Mockito.when(traineeRepository.findTraineeByUser_UserName(username))
                .thenReturn(Optional.of(trainee));
        Mockito.when(traineeMapper.toDto(trainee)).thenReturn(traineeDto);

        TraineeDto result = traineeService.getTraineeByUsername(username);

        Assertions.assertEquals(traineeDto, result);
        Mockito.verify(traineeRepository).findTraineeByUser_UserName(username);
        Mockito.verify(traineeMapper).toDto(trainee);
    }

    @Test
    void getTraineeByUsername_ThrowsException() {
        String username = "Ali.Valiyev";
        Mockito.when(traineeRepository.findTraineeByUser_UserName(username)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> traineeService.getTraineeByUsername(username));
        Assertions.assertEquals("Trainee not found", exception.getMessage());
        Mockito.verify(traineeMapper, Mockito.never()).toDto(Mockito.any());
    }

    @Test
    void changePassword_Success() {
        String username = "Ali.Valiyev";
        String oldPassword = "123";
        String newPassword = "111";
        String encodedPassword = "24241312rergetreteg";
        ChangeLoginDto dto = new ChangeLoginDto(username, oldPassword, newPassword);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, oldPassword);
        Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(authenticationManager.authenticate(usernamePasswordAuthenticationToken)).thenReturn(authentication);
        Mockito.when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        Mockito.doNothing().when(traineeRepository).changePassword(dto.username(), encodedPassword);

        traineeService.changePassword(dto);

        Mockito.verify(authenticationManager).authenticate(usernamePasswordAuthenticationToken);
        Mockito.verify(passwordEncoder).encode(newPassword);
        Mockito.verify(traineeRepository).changePassword(dto.username(), encodedPassword);
    }

    @Test
    void changePassword_ThrowsException() {
        ChangeLoginDto changeLoginDto = new ChangeLoginDto("Ali.Valiyev", "123", "111");
        Mockito.when(authenticationManager.authenticate(Mockito.any()))
                .thenThrow(BadCredentialsException.class);

        Assertions.assertThrows(BadCredentialsException.class, () -> traineeService.changePassword(changeLoginDto));

        Mockito.verify(passwordEncoder, Mockito.never()).encode(Mockito.any());
        Mockito.verify(traineeRepository, Mockito.never()).changePassword(Mockito.any(), Mockito.any());
    }

    @Test
    void update_Success() {
        String username = "Ali";
        TraineeUpdateDto dto = new TraineeUpdateDto("Ali", "Valiyev", true,
                new Date(), "Navai");
        Trainee trainee = new Trainee();
        TraineeDto traineeDto = new TraineeDto("Ali", "Valiyev", true, new Date(), "Navai", null);

        Mockito.when(traineeRepository.findTraineeByUser_UserName(username)).thenReturn(Optional.of(trainee));
        Mockito.when(traineeMapper.partialUpdate(dto, trainee)).thenReturn(trainee);
        Mockito.when(traineeRepository.save(trainee)).thenReturn(trainee);
        Mockito.when(traineeMapper.toDto(trainee)).thenReturn(traineeDto);

        traineeService.update(username, dto);

        Mockito.verify(traineeRepository).findTraineeByUser_UserName(username);
        Mockito.verify(traineeMapper).partialUpdate(dto, trainee);
        Mockito.verify(traineeRepository).save(trainee);
        Mockito.verify(traineeMapper).toDto(trainee);
    }

    @Test
    void update_ThrowsException() {
        String username = "Ali";
        TraineeUpdateDto dto = new TraineeUpdateDto("Ali", "Valiyev", true,
                new Date(), "Navai");
        Mockito.when(traineeRepository.findTraineeByUser_UserName(username)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> traineeService.update(username, dto));
        Assertions.assertEquals("Trainee not found", exception.getMessage());

        Mockito.verify(traineeRepository).findTraineeByUser_UserName(username);
        Mockito.verify(traineeMapper, Mockito.never()).partialUpdate(Mockito.any(), Mockito.any());
        Mockito.verify(traineeRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(traineeMapper, Mockito.never()).toDto(Mockito.any());
    }

    @Test
    void activateOrDeactivateTrainee_Success() {
        String username = "Ali";
        Mockito.when(traineeRepository.existsByUser_UserName(username)).thenReturn(true);
        Mockito.doNothing().when(traineeRepository).activateOrDeactivateTrainee(username);

        traineeService.activateOrDeactivateTrainee(username);

        Mockito.verify(traineeRepository).existsByUser_UserName(username);
        Mockito.verify(traineeRepository).activateOrDeactivateTrainee(username);
    }

    @Test
    void activateOrDeactivateTrainee_throwsException() {
        String username = "Ali";
        Mockito.when(traineeRepository.existsByUser_UserName(username)).thenReturn(false);

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> traineeService.activateOrDeactivateTrainee(username));
        Assertions.assertEquals("Trainee not found", exception.getMessage());

        Mockito.verify(traineeRepository).existsByUser_UserName(username);
        Mockito.verify(traineeRepository, Mockito.never()).activateOrDeactivateTrainee(username);
    }

    @Test
    void deleteTraineeByUsername_Success() {
        String username = "Ali";
        Mockito.when(traineeRepository.existsByUser_UserName(username)).thenReturn(true);
        Mockito.doNothing().when(traineeRepository).deleteByUser_UserName(username);

        traineeService.deleteTraineeByUsername(username);

        Mockito.verify(traineeRepository).existsByUser_UserName(username);
        Mockito.verify(traineeRepository).deleteByUser_UserName(username);
    }

    @Test
    void deleteTraineeByUsername_ThrowsException() {
        String username = "Ali";
        Mockito.when(traineeRepository.existsByUser_UserName(username)).thenReturn(false);

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> traineeService.deleteTraineeByUsername(username));
        Assertions.assertEquals("Trainee not found", exception.getMessage());
        Mockito.verify(traineeRepository, Mockito.never()).deleteByUser_UserName(username);
    }

    @Test
    void getTraineeTrainingsList_Success() {
        String traineeUsername = "Ali";
        Date fromDate = new Date(2025, 3, 4);
        Date toDate = new Date(2025, 3, 7);
        String trainerName = "Botir";
        Integer trainingTypeId = 3;
        List<Training> trainings = new ArrayList<>();
        Mockito.when(traineeRepository.existsByUser_UserName(traineeUsername)).thenReturn(true);

        Mockito.when(trainingRepository.getTraineeTrainingsListByTraineeUsernameAndCriteria(traineeUsername, fromDate, toDate, trainerName, trainingTypeId))
                .thenReturn(trainings);
        Mockito.when(trainingMapper.toDtoList(trainings)).thenReturn(new ArrayList<>());

        traineeService.getTraineeTrainingsList(traineeUsername, fromDate, toDate, trainerName, trainingTypeId);

        Mockito.verify(traineeRepository).existsByUser_UserName(traineeUsername);
        Mockito.verify(trainingRepository).getTraineeTrainingsListByTraineeUsernameAndCriteria(traineeUsername, fromDate, toDate, trainerName, trainingTypeId);
        Mockito.verify(trainingMapper).toDtoList(trainings);
    }

    @Test
    void getTraineeTrainingsList_ThrowsException() {
        String traineeUsername = "Ali";
        Date fromDate = new Date(2025, 3, 4);
        Date toDate = new Date(2025, 3, 7);
        String trainerName = "Botir";
        Integer trainingTypeId = 3;
        Mockito.when(traineeRepository.existsByUser_UserName(traineeUsername)).thenReturn(false);

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> traineeService.getTraineeTrainingsList(traineeUsername, fromDate, toDate, trainerName, trainingTypeId));
        Assertions.assertEquals("Trainee not found", exception.getMessage());

        Mockito.verify(trainingRepository, Mockito.never()).getTraineeTrainingsListByTraineeUsernameAndCriteria(traineeUsername, fromDate, toDate, trainerName, trainingTypeId);
        Mockito.verify(trainingMapper, Mockito.never()).toDtoList(Mockito.any());
    }

    @Test
    void getTrainersListNotAssignedOnTrainee_Success() {
        String username = "Ali";
        Integer traineeId = 2;
        List<Integer> ids = List.of(3, 4);
        List<Trainer> notAssignedTrainers = new ArrayList<>();
        Mockito.when(traineeRepository.findTraineeIdByUsername(username)).thenReturn(Optional.of(traineeId));
        Mockito.when(trainerRepository.findTrainersIdNotAssignedOnTraineeByTraineeId(traineeId)).thenReturn(ids);
        Mockito.when(trainerRepository.findTrainersById(ids)).thenReturn(notAssignedTrainers);
        Mockito.when(trainerMapper.toShortDtoList(notAssignedTrainers)).thenReturn(new ArrayList<>());

        traineeService.getTrainersListNotAssignedOnTrainee(username);

        Mockito.verify(traineeRepository).findTraineeIdByUsername(username);
        Mockito.verify(trainerRepository).findTrainersIdNotAssignedOnTraineeByTraineeId(traineeId);
        Mockito.verify(trainerRepository).findTrainersById(ids);
        Mockito.verify(trainerMapper).toShortDtoList(notAssignedTrainers);
    }

    @Test
    void getTrainersListNotAssignedOnTrainee_ThrowsException(){
        String username = "Ali";
        Mockito.when(traineeRepository.findTraineeIdByUsername(username)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> traineeService.getTrainersListNotAssignedOnTrainee(username));
        Assertions.assertEquals("Trainee not found", exception.getMessage());

        Mockito.verify(traineeRepository).findTraineeIdByUsername(username);
        Mockito.verify(trainerRepository, Mockito.never()).findTrainersIdNotAssignedOnTraineeByTraineeId(Mockito.any());
        Mockito.verify(trainerRepository, Mockito.never()).findTrainersById(Mockito.any());
        Mockito.verify(trainerMapper, Mockito.never()).toShortDtoList(Mockito.any());
    }

    @Test
    void updateTraineeTrainerList_Success() {
        Trainee trainee = new Trainee();
        List<Trainer> trainers = new ArrayList<>();
        UpdateTraineeTrainersListDto dto = new UpdateTraineeTrainersListDto("Ali", List.of("Botir", "Sanjar"));
        Mockito.when(traineeRepository.findTraineeByUser_UserName(dto.traineeUsername())).thenReturn(Optional.of(trainee));
        Mockito.when(trainerRepository.findTrainersByUser_UserNameIn(dto.trainers())).thenReturn(trainers);
        Mockito.when(traineeRepository.save(trainee)).thenReturn(trainee);
        Mockito.when(trainerMapper.toShortDtoList(trainers)).thenReturn(new ArrayList<>());

        traineeService.updateTraineeTrainerList(dto);

        Mockito.verify(traineeRepository).findTraineeByUser_UserName(dto.traineeUsername());
        Mockito.verify(trainerRepository).findTrainersByUser_UserNameIn(dto.trainers());
        Assertions.assertEquals(trainers, trainee.getTrainers());
        Mockito.verify(traineeRepository).save(trainee);
        Mockito.verify(trainerMapper).toShortDtoList(trainers);
    }

    @Test
    void updateTraineeTrainerList_ThrowsException() {
        UpdateTraineeTrainersListDto dto = new UpdateTraineeTrainersListDto("Ali", List.of("Botir", "Sanjar"));
        Mockito.when(traineeRepository.findTraineeByUser_UserName(dto.traineeUsername())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> traineeService.updateTraineeTrainerList(dto));
        Assertions.assertEquals("Trainee not found", exception.getMessage());

        Mockito.verify(trainerRepository, Mockito.never()).findTrainerByUser_UserName(Mockito.any());
        Mockito.verify(traineeRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(trainerMapper, Mockito.never()).toShortDtoList(Mockito.any());
    }
}