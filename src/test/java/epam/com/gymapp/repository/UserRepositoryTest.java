package epam.com.gymapp.repository;

import epam.com.gymapp.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setUserName("John.Doe");
        user1.setPassword("111");
        user1.setActive(true);

        User user2 = new User();
        user2.setFirstName("John");
        user2.setLastName("Doe");
        user2.setUserName("John.Doe1");
        user2.setPassword("222");
        user2.setActive(true);

        User user3 = new User();
        user3.setFirstName("John");
        user3.setLastName("Doe");
        user3.setUserName("John.Doe2");
        user3.setPassword("333");
        user3.setActive(true);

        userRepository.saveAll(List.of(user1, user2, user3));
    }

    @Test
    void countByFirstNameAndLastName() {
        Integer count = userRepository.countByFirstNameAndLastName("John", "Doe");
        Assertions.assertEquals(3, count);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}