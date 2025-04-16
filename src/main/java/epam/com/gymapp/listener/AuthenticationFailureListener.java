package epam.com.gymapp.listener;

import epam.com.gymapp.entity.User;
import epam.com.gymapp.repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    private final UserRepository userRepository;

    public AuthenticationFailureListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        Authentication authentication = event.getAuthentication();
        String username = (String) authentication.getPrincipal();
        if (userRepository.findByUserName(username).isPresent()) {
            User user = userRepository.findByUserName(username).get();
            int loginAttempts = user.getLoginAttempts();
            Date lockTime = user.getLockTime();
            if (lockTime != null && lockTime.before(new Date())) {   // Unlock the user if lock time has expired
                user.setLoginAttempts(1);
                user.setLockTime(null);
            }
            else if (lockTime == null ) {        // if not locked yet
                user.setLoginAttempts(loginAttempts + 1);
                if (user.getLoginAttempts() >= 3) {
                    user.setLockTime(new Date(System.currentTimeMillis() + 5 * 60 * 1000));
                }
            }
            userRepository.save(user);
        }
    }
}
