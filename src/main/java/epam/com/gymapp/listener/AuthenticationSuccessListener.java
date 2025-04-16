package epam.com.gymapp.listener;

import epam.com.gymapp.config.security.CustomUserDetails;
import epam.com.gymapp.entity.User;
import epam.com.gymapp.repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {
    private final UserRepository userRepository;

    public AuthenticationSuccessListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        if (userRepository.findByUserName(username).isPresent()) {
            User user = userRepository.findByUserName(username).get();
            user.setLoginAttempts(0);
            user.setLockTime(null);
            userRepository.save(user);
        }
    }
}
