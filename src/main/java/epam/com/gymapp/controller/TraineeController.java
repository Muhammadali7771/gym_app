package epam.com.gymapp.controller;

import epam.com.gymapp.dto.*;
import epam.com.gymapp.dto.trainee.TraineeCreateDto;
import epam.com.gymapp.dto.trainee.TraineeDto;
import epam.com.gymapp.dto.trainee.TraineeUpdateDto;
import epam.com.gymapp.dto.trainer.TrainerShortDto;
import epam.com.gymapp.dto.training.TrainingDto;
import epam.com.gymapp.service.TraineeService;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("gym.com/api/trainee")
public class TraineeController {
    private final TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @GetMapping
    @Operation(summary = "Get trainee with the supplied username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved trainee with the supplied username"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @Counted(value = "api.trainee.get", description = "a number of requests to GET /api/trainee endpoint")
    @Timed(value = "api.trainee.get.time", description = "time taken for GET /api/trainee endpoint")
    public ResponseEntity<TraineeDto> getTraineeByUsername(@RequestParam("username") String username) {
        return ResponseEntity.ok(traineeService.getTraineeByUsername(username));
    }

    @PostMapping
    @Operation(summary = "Register a new trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New Trainee successfully has been registered"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")})
    @Counted(value = "api.trainee.post", description = "a number of requests to POST /api/trainee endpoint")
    @Timed(value = "api.trainee.post.time", description = "time taken for POST /api/trainee endpoint")
    public ResponseEntity<RegistrationResponseDto> register(@RequestBody @Valid TraineeCreateDto dto) {
        return new ResponseEntity<>(traineeService.create(dto), HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(summary = "Update trainee profile information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the trainee information"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "404", description = "The Resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @Counted(value = "api.trainee.put", description = "a number of requests to PUT /api/trainee endpoint")
    @Timed(value = "api.trainee.put.time", description = "time taken for PUT /api/trainee endpoint")
    public ResponseEntity<TraineeDto> update(@RequestParam("username") String username, @RequestBody TraineeUpdateDto dto) {
        TraineeDto updatedTrainee = traineeService.update(username, dto);
        return ResponseEntity.ok(updatedTrainee);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate a trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication failed"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @Counted(value = "api.trainee.login.post", description = "a number of requests to POST /api/trainee endpoint")
    @Timed(value = "api.trainee.login.post.time", description = "time taken for POST /api/trainee endpoint")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequestDto dto) {
        TokenResponse token = traineeService.login(dto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @DeleteMapping
    @Operation(summary = "Deleting the specific trainee with the supplied username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deletes the specific trainee"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @Counted(value = "api.trainee.delete", description = "a number of requests to DELETE /api/trainee endpoint")
    @Timed(value = "api.trainee.delete.time", description = "time taken for DELETE /api/trainee endpoint")
    public ResponseEntity<Void> delete(@RequestParam("username") String username) {
        traineeService.deleteTraineeByUsername(username);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-login")
    @Operation(summary = "Change the password of the trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password has been changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @Counted(value = "api.trainee.change-login.put", description = "a number of requests to PUT /api/trainee/change-login endpoint")
    @Timed(value = "api.trainee.change-login.put.time", description = "time taken for /api/trainee/change-login endpoint")
    public ResponseEntity<Void> changeLogin(@RequestBody @Valid ChangeLoginDto dto) {
        traineeService.changePassword(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/trainer-list")
    @Operation(summary = "Update trainee's trainer list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updates the trainee's trainer list"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @Counted(value = "api.trainee.trainer-list.put", description = "a number of requests to PUT /api/trainee/trainer-list endpoint")
    @Timed(value = "api.trainee.trainer-list.put.time", description = "time taken for PUT /api/trainee/trainer-list endpoint")
    public ResponseEntity<List<TrainerShortDto>> updateTraineeTrainerList(@RequestBody UpdateTraineeTrainersListDto dto) {
        List<TrainerShortDto> trainers = traineeService.updateTraineeTrainerList(dto);
        return new ResponseEntity<>(trainers, HttpStatus.OK);
    }

    @GetMapping("training-list")
    @Operation(summary = "Get trainee trainings list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieves the trainings of the trainee supplied with the username by criteria"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @Counted(value = "api.trainee.training-list.get", description = "a number of requests to GET /api/trainee/training-list endpoint")
    @Timed(value = "api.trainee.training-list.get.time", description = "time taken for GET /api/trainee/training-list endpoint")
    public ResponseEntity<List<TrainingDto>> getTrainingList(@RequestParam(name = "username") String traineeUsername,
                                                             @RequestParam(name = "period-from", required = false) Date periodFrom,
                                                             @RequestParam(name = "period-to", required = false) Date periodTo,
                                                             @RequestParam(name = "trainer-name", required = false) String trainerName,
                                                             @RequestParam(name = "training-type", required = false) Integer trainingTypeId) {
        List<TrainingDto> trainings = traineeService.getTraineeTrainingsList(traineeUsername, periodFrom, periodTo, trainerName, trainingTypeId);
        return ResponseEntity.ok(trainings);
    }

    @PatchMapping("/change-status/{username}")
    @Operation(summary = "Activate/De-Activate trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully activates or deactivates the trainee"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "404", description = "resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @Counted(value = "api.trainee.change-status", description = "a number of requests to PATCH /api/trainee/change-status/{username} endpoint")
    @Timed(value = "api.trainee.change-status.time", description = "time taken for PATCH /api/trainee/change-status/{username} endpoint")
    public ResponseEntity<Void> activateOrDeactivateTrainee(@PathVariable String username) {
        traineeService.activateOrDeactivateTrainee(username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unassigned-trainers")
    @Operation(summary = "Get the trainers with is not assigned on trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieves trainers which is not assigned on trainee"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @Counted(value = "api.trainee.unassigned-trainers.get", description = "a number of requests to PATCH /api/trainee/unassigned-trainers endpoint")
    @Timed(value = "api.trainee.unassigned-trainers.get.time", description = "time taken for PATCH /api/trainee/unassigned-trainers endpoint")
    public ResponseEntity<List<TrainerShortDto>> getUnassignedTrainers(@RequestParam("username") String traineeUsername) {
        List<TrainerShortDto> trainers = traineeService
                .getTrainersListNotAssignedOnTrainee(traineeUsername);
        return new ResponseEntity<>(trainers, HttpStatus.OK);
    }

}
