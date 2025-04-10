package epam.com.gym_app.util;

import epam.com.gym_app.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsernamePasswordGeneratorTest {
    @InjectMocks
    private UsernamePasswordGenerator usernamePasswordGenerator;
    @Mock
    private UserRepository userRepository;

    @Test
    void generateUsername() {
        String firstName = "ali";
        String lastName = "valiyev";

        Mockito.when(userRepository.countByFirstNameAndLastName(firstName, lastName)).thenReturn(3);
        String generateUsername = usernamePasswordGenerator.generateUsername(firstName, lastName);

        Assertions.assertEquals("ali.valiyev3", generateUsername);
        Mockito.verify(userRepository).countByFirstNameAndLastName(firstName, lastName);
    }

    @Test
    void generatePassword() {
        String generatedPassword = usernamePasswordGenerator.generatePassword();
        Assertions.assertNotNull(generatedPassword);
        Assertions.assertEquals(10, generatedPassword.length());
    }
}