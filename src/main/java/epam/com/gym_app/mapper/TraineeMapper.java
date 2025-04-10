package epam.com.gym_app.mapper;


import epam.com.gym_app.dto.trainee.TraineeCreateDto;
import epam.com.gym_app.dto.trainee.TraineeDto;
import epam.com.gym_app.dto.trainee.TraineeUpdateDto;
import epam.com.gym_app.dto.trainer.TrainerShortDto;
import epam.com.gym_app.entity.Trainee;
import epam.com.gym_app.entity.Trainer;
import epam.com.gym_app.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TraineeMapper {

    public Trainee toEntity(TraineeCreateDto dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());

        Trainee trainee = new Trainee();
        trainee.setAddress(dto.address());
        trainee.setDateOfBirth(dto.dateOfBirth());
        trainee.setUser(user);

        return trainee;
    }

    public Trainee partialUpdate(TraineeUpdateDto dto, Trainee trainee) {
        User user = trainee.getUser();
        if (dto.firstName() != null) {
            user.setFirstName(dto.firstName());
        }
        if (dto.lastName() != null) {
            user.setLastName(dto.lastName());
        }
        user.setActive(dto.isActive());

        if (dto.address() != null) {
            trainee.setAddress(dto.address());
        }
        if (dto.dateOfBirth() != null) {
            trainee.setDateOfBirth(dto.dateOfBirth());
        }
        return trainee;
    }

    public TraineeDto toDto(Trainee trainee) {
        if (trainee == null) {
            return null;
        }

        User user = trainee.getUser();

        List<Trainer> trainers = trainee.getTrainers();
        List<TrainerShortDto> trainerShortDtos = new ArrayList<>();
        for (Trainer trainer : trainers) {
            User trainerUser = trainer.getUser();
            TrainerShortDto trainerShortDto = new TrainerShortDto(trainerUser.getUserName(),trainerUser.getFirstName(), trainerUser.getLastName(),
                    trainerUser.isActive(), trainer.getSpecialization().getId());
            trainerShortDtos.add(trainerShortDto);
        }

        TraineeDto traineeDto = new TraineeDto(user.getFirstName(), user.getLastName(), user.isActive(), trainee.getDateOfBirth(),
                trainee.getAddress(), trainerShortDtos);
        return traineeDto;
    }
}
