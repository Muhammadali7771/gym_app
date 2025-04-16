package epam.com.gymapp.service;

import epam.com.gymapp.dto.trainingtype.TrainingTypeDto;
import epam.com.gymapp.entity.TrainingType;

import java.util.List;

public interface TrainingTypeService {

    TrainingType getTrainingTypeById(Integer id);

    List<TrainingTypeDto> getAllTrainingTypes();
}
