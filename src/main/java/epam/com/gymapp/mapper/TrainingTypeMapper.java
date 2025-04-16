package epam.com.gymapp.mapper;

import epam.com.gymapp.dto.trainingtype.TrainingTypeDto;
import epam.com.gymapp.entity.TrainingType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainingTypeMapper {
    public TrainingTypeDto toDto(TrainingType trainingType) {
        TrainingTypeDto trainingTypeDto = new TrainingTypeDto(trainingType.getId(), trainingType.getTrainingTypeName());
        return trainingTypeDto;
    }

    public List<TrainingTypeDto> toDtoList(List<TrainingType> trainingTypes) {
        return trainingTypes.stream().map(this::toDto).toList();
    }
}
