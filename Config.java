package lsit;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class Config {
    
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
            .csrf(c -> c.disable())
            .oauth2Login(withDefaults())
            .authorizeHttpRequests(a -> a
                .requestMatchers("/user").authenticated()
                .requestMatchers(HttpMethod.POST, "/Car").authenticated()
                .requestMatchers(HttpMethod.POST, "/CarRequest").authenticated()
                .requestMatchers(HttpMethod.POST, "/Clown").authenticated()
                .requestMatchers(HttpMethod.POST, "/Person").authenticated()
                .anyRequest().permitAll()
            )
            ;

        return http.build();
    }

}
