package epam.com.gymapp.mapper;

import epam.com.gymapp.dto.trainingtype.TrainingTypeDto;
import epam.com.gymapp.entity.TrainingType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class TrainingTypeMapperTest {

    @Autowired
    private TrainingTypeMapper trainingTypeMapper;

    @Test
    void toDto() {
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1);
        String trainingTypeName = "football";
        trainingType.setTrainingTypeName(trainingTypeName);

        TrainingTypeDto dto = trainingTypeMapper.toDto(trainingType);

        Assertions.assertEquals(1, dto.trainingTypeId());
        Assertions.assertEquals(trainingTypeName, dto.trainingTypeName());
    }

    @Test
    void toDtoList() {
        TrainingType trainingType1 = new TrainingType();
        trainingType1.setId(1);
        String football = "Football";
        trainingType1.setTrainingTypeName(football);

        TrainingType trainingType2 = new TrainingType();
        trainingType2.setId(2);
        String basketball = "Basketball";
        trainingType2.setTrainingTypeName(basketball);

        List<TrainingTypeDto> dtoList = trainingTypeMapper.toDtoList(List.of(trainingType1, trainingType2));

        Assertions.assertEquals(2, dtoList.size());
        TrainingTypeDto trainingTypeDto = dtoList.get(0);
        TrainingTypeDto trainingTypeDto1 = dtoList.get(1);

        Assertions.assertNotNull(trainingTypeDto);
        Assertions.assertNotNull(trainingTypeDto1);
        Assertions.assertEquals(1, trainingTypeDto.trainingTypeId());
        Assertions.assertEquals(football, trainingTypeDto.trainingTypeName());
        Assertions.assertEquals(2, trainingTypeDto1.trainingTypeId());
        Assertions.assertEquals(basketball, trainingTypeDto1.trainingTypeName());
    }
}