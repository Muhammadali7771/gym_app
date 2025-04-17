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

    public Trainer toEntity(TrainerCreateDto dto, TrainingType trainingType) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setLastName(dto.lastName());
        user.setFirstName(dto.firstName());
        user.setIsActive(true);

        Trainer trainer = new Trainer();
        trainer.setSpecialization(trainingType);
        trainer.setUser(user);

        return trainer;
    }

    public Trainer partialUpdate(TrainerUpdateDto dto, TrainingType trainingType, Trainer trainer) {
        User user = trainer.getUser();
        if (dto.firstName() != null) {
            user.setFirstName(dto.firstName());
        }
        if (dto.lastName() != null) {
            user.setLastName(dto.lastName());
        }
        if (dto.isActive() != null){
            user.setIsActive(dto.isActive());
        }
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
                    traineeUser.getIsActive());
            traineeShortDtos.add(traineeShortDto);
        }
        TrainerDto trainerDto = new TrainerDto(user.getFirstName(), user.getLastName(), user.getIsActive(), trainer.getSpecialization().getId(), traineeShortDtos);
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
                user.getIsActive(),
                trainer.getSpecialization().getId());
        return trainerShortDto;
    }

    public List<TrainerShortDto> toShortDtoList(List<Trainer> trainers) {
        return trainers.stream().map(this::toShortDto).toList();
    }
}
