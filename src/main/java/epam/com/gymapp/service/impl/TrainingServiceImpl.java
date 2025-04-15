package epam.com.gymapp.service.impl;

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
import epam.com.gymapp.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainingMapper trainingMapper;

    @Override
    public void create(TrainingCreateDto dto) {
        Trainer trainer = trainerRepository.findTrainerByUser_UserName(dto.trainerUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found"));
        Trainee trainee = traineeRepository.findTraineeByUser_UserName(dto.traineeUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found"));
        TrainingType trainingType = trainingTypeRepository.findTrainingTypeById(dto.trainingTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Training type not found"));
        Training training = trainingMapper.toEntity(dto, trainer, trainee, trainingType);
        trainingRepository.save(training);
    }
}
