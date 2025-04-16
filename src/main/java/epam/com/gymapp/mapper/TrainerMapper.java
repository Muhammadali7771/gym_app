package epam.com.gymapp.mapper;

import epam.com.gymapp.dto.trainee.TraineeShortDto;
import epam.com.gymapp.dto.trainer.TrainerCreateDto;
import epam.com.gymapp.dto.trainer.TrainerDto;
import epam.com.gymapp.dto.trainer.TrainerShortDto;
import epam.com.gymapp.dto.trainer.TrainerUpdateDto;
import epam.com.gymapp.entity.Trainee;
import epam.com.gymapp.entity.Trainer;
import epam.com.gymapp.entity.TrainingType;
import epam.com.gymapp.entity.User;
import epam.com.gymapp.service.impl.TrainingTypeServiceImpl;
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
        user.setActive(true);

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
