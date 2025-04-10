package epam.com.gym_app.mapper;

import epam.com.gym_app.dto.trainee.TraineeShortDto;
import epam.com.gym_app.dto.trainer.TrainerCreateDto;
import epam.com.gym_app.dto.trainer.TrainerDto;
import epam.com.gym_app.dto.trainer.TrainerShortDto;
import epam.com.gym_app.dto.trainer.TrainerUpdateDto;
import epam.com.gym_app.entity.Trainee;
import epam.com.gym_app.entity.Trainer;
import epam.com.gym_app.entity.TrainingType;
import epam.com.gym_app.entity.User;
import epam.com.gym_app.service.impl.TrainingTypeServiceImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TrainerMapper {
    private final TrainingTypeServiceImpl trainingTypeService;

    public TrainerMapper(TrainingTypeServiceImpl trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    public Trainer toEntity(TrainerCreateDto dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setLastName(dto.lastName());
        user.setFirstName(dto.firstName());

        Trainer trainer = new Trainer();
        Integer trainingTypeId = dto.specializationId();
        TrainingType trainingType = trainingTypeService.getTrainingTypeById(trainingTypeId);
        trainer.setSpecialization(trainingType);
        trainer.setUser(user);

        return trainer;
    }

    public Trainer partialUpdate(TrainerUpdateDto dto, Trainer trainer) {
        User user = trainer.getUser();
        if (dto.firstName() != null) {
            user.setFirstName(dto.firstName());
        }
        if (dto.lastName() != null) {
            user.setLastName(dto.lastName());
        }
        user.setActive(dto.isActive());

        Integer trainingTypeId = dto.specializationId();
        TrainingType trainingType = trainingTypeService.getTrainingTypeById(trainingTypeId);
        trainer.setSpecialization(trainingType);

        return trainer;
    }

    public TrainerDto toDto(Trainer trainer) {
        if (trainer == null) {
            return null;
        }

        User user = trainer.getUser();

        List<Trainee> trainees = trainer.getTrainees();
        List<TraineeShortDto> traineeShortDtos = new ArrayList<>();
        for (Trainee trainee : trainees) {
            User traineeUser = trainee.getUser();
            TraineeShortDto traineeShortDto = new TraineeShortDto(traineeUser.getUserName(),traineeUser.getFirstName(), traineeUser.getLastName(),
                    traineeUser.isActive());
            traineeShortDtos.add(traineeShortDto);
        }
        TrainerDto trainerDto = new TrainerDto(user.getFirstName(), user.getLastName(), user.isActive(), trainer.getSpecialization().getId(), traineeShortDtos);
        return trainerDto;
    }

    public TrainerShortDto toShortDto(Trainer trainer) {
        if (trainer == null) {
            return null;
        }
        User user = trainer.getUser();

        TrainerShortDto trainerShortDto = new TrainerShortDto(user.getFirstName(),
                user.getLastName(),
                user.getUserName(),
                user.isActive(),
                trainer.getSpecialization().getId());
        return trainerShortDto;
    }

    public List<TrainerShortDto> toShortDtoList(List<Trainer> trainers) {
        return trainers.stream().map(this::toShortDto).toList();
    }
}
