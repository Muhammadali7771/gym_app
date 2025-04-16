package epam.com.gymapp.service.impl;


import epam.com.gymapp.dto.trainingtype.TrainingTypeDto;
import epam.com.gymapp.entity.TrainingType;
import epam.com.gymapp.exception.ResourceNotFoundException;
import epam.com.gymapp.mapper.TrainingTypeMapper;
import epam.com.gymapp.repository.TrainingTypeRepository;
import epam.com.gymapp.service.TrainingTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainingTypeMapper trainingTypeMapper;

    public TrainingTypeServiceImpl(TrainingTypeRepository trainingTypeRepository, TrainingTypeMapper trainingTypeMapper) {
        this.trainingTypeRepository = trainingTypeRepository;
        this.trainingTypeMapper = trainingTypeMapper;
    }

    @Override
    public TrainingType getTrainingTypeById(Integer id) {
        return trainingTypeRepository.findTrainingTypeById(id).orElseThrow(
                 () -> {
                     log.warn("Training type not found with id : {}", id);
                     return new ResourceNotFoundException("Training type not found");
                 });
    }

    @Override
    public List<TrainingTypeDto> getAllTrainingTypes() {
        List<TrainingType> allTrainingTypes = trainingTypeRepository.findAll();
        return trainingTypeMapper.toDtoList(allTrainingTypes);
    }
}
