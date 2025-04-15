package epam.com.gymapp.service;

import epam.com.gymapp.dto.training.TrainingCreateDto;
import epam.com.gymapp.entity.Trainee;
import epam.com.gymapp.entity.Trainer;
import epam.com.gymapp.entity.Training;
import epam.com.gymapp.entity.TrainingType;
import epam.com.gymapp.exception.ResourceNotFoundException;
import epam.com.gymapp.mapper.TrainingMapper;
import epam.com.gymapp.repository.TraineeRepository;
import epam.com.gymapp.repository.TrainerRepository;
import epam.com.gymapp.repository.TrainingRepository;
import epam.com.gymapp.repository.TrainingTypeRepository;
import epam.com.gymapp.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {
    @InjectMocks
    private TrainingServiceImpl trainingService;
    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TrainingTypeRepository trainingTypeRepository;
    @Mock
    private TrainingMapper trainingMapper;
    TrainingCreateDto trainingCreateDto;
    Trainer trainer;
    Trainee trainee;
    TrainingType trainingType;
    Training training;

    @BeforeEach
    void setUp() {
        trainingCreateDto = new TrainingCreateDto(
                "Ali.Valiyev", "Botir.Sobirov", "training 1",
                new Date(2025, 03, 28), 2, 50);
        trainer = new Trainer();
        trainer.setId(1);
        trainee = new Trainee();
        trainee.setId(1);
        trainingType = new TrainingType();
        trainingType.setId(1);
        training = new Training();
        training.setId(1);
    }

    @Test
    void create_Success() {

        Mockito.when(trainerRepository.findTrainerByUser_UserName(trainingCreateDto.trainerUsername())).thenReturn(Optional.of(trainer));
        Mockito.when(traineeRepository.findTraineeByUser_UserName(trainingCreateDto.traineeUsername())).thenReturn(Optional.of(trainee));
        Mockito.when(trainingTypeRepository.findTrainingTypeById(trainingCreateDto.trainingTypeId())).thenReturn(Optional.of(trainingType));
        Mockito.when(trainingMapper.toEntity(trainingCreateDto, trainer, trainee, trainingType)).thenReturn(training);

        trainingService.create(trainingCreateDto);

        Mockito.verify(trainerRepository, Mockito.times(1)).findTrainerByUser_UserName(trainingCreateDto.trainerUsername());
        Mockito.verify(traineeRepository, Mockito.times(1)).findTraineeByUser_UserName(trainingCreateDto.traineeUsername());
        Mockito.verify(trainingTypeRepository, Mockito.times(1)).findTrainingTypeById(trainingCreateDto.trainingTypeId());
        Mockito.verify(trainingMapper, Mockito.times(1)).toEntity(trainingCreateDto, trainer, trainee, trainingType);
        Mockito.verify(trainingRepository, Mockito.times(1)).save(training);
    }

    @Test
    void create_TrainerNotFound() {
        Mockito.when(trainerRepository.findTrainerByUser_UserName(trainingCreateDto.trainerUsername())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> trainingService.create(trainingCreateDto));
        Assertions.assertEquals("Trainer not found", exception.getMessage());
    }

    @Test
    void create_TraineeNotFound() {
        Mockito.when(trainerRepository.findTrainerByUser_UserName(trainingCreateDto.trainerUsername()))
                .thenReturn(Optional.of(trainer));
        Mockito.when(traineeRepository.findTraineeByUser_UserName(trainingCreateDto.traineeUsername()))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> trainingService.create(trainingCreateDto));
        Assertions.assertEquals("Trainee not found", exception.getMessage());
    }

    @Test
    void create_TrainingTypeNotFound() {
        Mockito.when(trainerRepository.findTrainerByUser_UserName(trainingCreateDto.trainerUsername()))
                .thenReturn(Optional.of(trainer));
        Mockito.when(traineeRepository.findTraineeByUser_UserName(trainingCreateDto.traineeUsername()))
                .thenReturn(Optional.of(trainee));
        Mockito.when(trainingTypeRepository.findTrainingTypeById(trainingCreateDto.trainingTypeId()))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> trainingService.create(trainingCreateDto));

        Assertions.assertEquals("Training type not found", exception.getMessage());
        Mockito.verify(trainingRepository, Mockito.never()).save(training);
    }
}