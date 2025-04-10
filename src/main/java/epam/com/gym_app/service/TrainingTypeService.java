package epam.com.gym_app.service;

import epam.com.gym_app.dto.training_type.TrainingTypeDto;
import epam.com.gym_app.entity.TrainingType;

import java.util.List;

public interface TrainingTypeService {

    TrainingType getTrainingTypeById(Integer id);

    List<TrainingTypeDto> getAllTrainingTypes();
}
