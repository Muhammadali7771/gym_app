package epam.com.gymapp.service;

import epam.com.gymapp.dto.trainingtype.TrainingTypeDto;
import epam.com.gymapp.entity.TrainingType;
import epam.com.gymapp.exception.ResourceNotFoundException;
import epam.com.gymapp.mapper.TrainingTypeMapper;
import epam.com.gymapp.repository.TrainingTypeRepository;
import epam.com.gymapp.service.impl.TrainingTypeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceTest {
    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;
    @Mock
    private TrainingTypeMapper trainingTypeMapper;

    @Test
    void getTrainingTypeById_Success() {
        Integer id = 1;
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1);
        trainingType.setTrainingTypeName("Fitness");
        Mockito.when(trainingTypeRepository.findTrainingTypeById(id)).thenReturn(Optional.of(trainingType));

        TrainingType foundTrainingType = trainingTypeService.getTrainingTypeById(id);
        Assertions.assertNotNull(foundTrainingType);
        Assertions.assertEquals(trainingType.getId(), foundTrainingType.getId());
        Assertions.assertEquals(trainingType.getTrainingTypeName(), foundTrainingType.getTrainingTypeName());
        Mockito.verify(trainingTypeRepository, Mockito.times(1)).findTrainingTypeById(id);
    }

    @Test
    void getTrainingTypeById_ThrowsResourceNotFoundException() {
        Integer id = 7;

        Mockito.when(trainingTypeRepository.findTrainingTypeById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> trainingTypeService.getTrainingTypeById(id));
        Assertions.assertEquals("Training type not found", exception.getMessage());
        Mockito.verify(trainingTypeRepository, Mockito.times(1)).findTrainingTypeById(id);
    }

    @Test
    void getAllTrainingTypes() {
        TrainingType trainingType1 = new TrainingType();
        trainingType1.setId(1);
        trainingType1.setTrainingTypeName("Football");
        TrainingType trainingType2 = new TrainingType();
        trainingType2.setId(2);
        trainingType2.setTrainingTypeName("Basketball");
        List<TrainingType> trainingTypes = List.of(trainingType1, trainingType2);
        List<TrainingTypeDto> trainingTypeDtos = List.of(new TrainingTypeDto(1, "Football"),
                new TrainingTypeDto(2, "Basketball"));

        Mockito.when(trainingTypeRepository.findAll()).thenReturn(trainingTypes);
        Mockito.when(trainingTypeMapper.toDtoList(trainingTypes)).thenReturn(trainingTypeDtos);

        List<TrainingTypeDto> resultTrainingTypeDtos = trainingTypeService.getAllTrainingTypes();

        Assertions.assertEquals(2, resultTrainingTypeDtos.size());
        Assertions.assertEquals(trainingTypeDtos.get(0), resultTrainingTypeDtos.get(0));
        Assertions.assertEquals(trainingTypeDtos.get(1), resultTrainingTypeDtos.get(1));
        Mockito.verify(trainingTypeRepository, Mockito.times(1)).findAll();
        Mockito.verify(trainingTypeMapper, Mockito.times(1)).toDtoList(trainingTypes);
    }
}