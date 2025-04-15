package epam.com.gymapp.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeLoginDto(
        @NotBlank(message = "username can not be null and blank")
        String username,
        @NotBlank(message = "old password can not be null and blank")
        String oldPassword,
        @NotBlank(message = "New password can not be null and blank")
        String newPassword) {
}
