package epam.com.gym_app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {
    @Bean
    public OpenAPI springOpenApi(){
        return new OpenAPI()
                .info(new Info().title("Gym Project")
                        .description("Gym Project Open Api")
                        .contact(new Contact().email("akbarovmuhammadali7777@gmail.com")
                                .name("Muhammadali")));
    }
}