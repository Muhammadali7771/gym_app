package epam.com.gymapp.util;

import epam.com.gymapp.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class UsernamePasswordGenerator {
    private final UserRepository userRepository;

    public UsernamePasswordGenerator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateUsername(String firstName, String lastName) {
        String username = firstName.concat(".").concat(lastName);
        Integer count = userRepository.countByFirstNameAndLastName(firstName, lastName);
        if (count != 0) {
            username = username.concat(String.valueOf(count));
        }
        return username;
    }

    public String generatePassword(){
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 10; i++){
            int a = random.nextInt(33, 127);
            password.append((char) a);
        }
        return password.toString();
    }
}
