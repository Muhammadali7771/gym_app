package epam.com.gym_app.controller;

import epam.com.gym_app.dto.training_type.TrainingTypeDto;
import epam.com.gym_app.service.TrainingTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("gym.com/api/training-type")
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;

    public TrainingTypeController(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    @GetMapping
    @Operation(summary = "Retrieves training types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieves the training types"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    public ResponseEntity<List<TrainingTypeDto>> getTrainingTypes() {
        List<TrainingTypeDto> allTrainingTypes = trainingTypeService.getAllTrainingTypes();
        return new ResponseEntity<>(allTrainingTypes, HttpStatus.OK);
    }
}
