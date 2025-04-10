package epam.com.gym_app.service.impl;

import epam.com.gym_app.dto.training.TrainingCreateDto;
import epam.com.gym_app.entity.Trainee;
import epam.com.gym_app.entity.Trainer;
import epam.com.gym_app.entity.Training;
import epam.com.gym_app.entity.TrainingType;
import epam.com.gym_app.exception.ResourceNotFoundException;
import epam.com.gym_app.mapper.TrainingMapper;
import epam.com.gym_app.repository.TraineeRepository;
import epam.com.gym_app.repository.TrainerRepository;
import epam.com.gym_app.repository.TrainingRepository;
import epam.com.gym_app.repository.TrainingTypeRepository;
import epam.com.gym_app.service.TrainingService;
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
