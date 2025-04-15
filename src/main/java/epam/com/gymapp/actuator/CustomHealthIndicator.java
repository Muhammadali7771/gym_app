package epam.com.gymapp.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        if (check()){
            return Health.up().withDetail("state", "success").build();
        }
        return Health.down().withDetail("state", "failed").build();
    }

    private boolean check(){
        return true;
    }
}
