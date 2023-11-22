package eliminatorias.eliminatorias.config;

import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static org.springframework.security.config.Customizer.withDefaults;



@Configuration
@EnableWebSecurity
public class SpringSecurity {
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        http.csrf().ignoringRequestMatchers("/graphql/**").csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());


        http.cors(withDefaults())
                .csrf(withDefaults());
                http.authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/registration/**").permitAll()
                        .requestMatchers("/login/**").permitAll()
                                .requestMatchers("home/list").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("home/index").hasAnyRole("ADMIN")
                                .requestMatchers("/arbitros/**").hasAnyRole("ADMIN")
                                .requestMatchers("/ciudads/**").hasAnyRole("ADMIN")
                                .requestMatchers("/estadios/**").hasAnyRole("ADMIN")
                                .requestMatchers("/jornadas/**").hasAnyRole("ADMIN")
                                .requestMatchers("/jugadors/**").hasAnyRole("ADMIN")
                                .requestMatchers("/paiss/**").hasAnyRole("ADMIN")
                                .requestMatchers("/partidos/**").hasAnyRole("ADMIN")
                                .requestMatchers("/seleccions/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/")
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll())
                .exceptionHandling().accessDeniedPage("/access-denied");
        return http.build();
    }
}