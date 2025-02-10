package com.schoolworld.feesPayment.config;

import com.schoolworld.feesPayment.filter.JwtAuthFilter;
import com.schoolworld.feesPayment.repository.UserRepository;
import com.schoolworld.feesPayment.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationEntryPointImplement authenticationEntryPointImplement;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailService(userRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers("/payment/retrieve-all").hasAnyAuthority("ROLE_MODERATOR","ROLE_ADMINISTRATOR","ROLE_CORRESPONDENT")
                        .requestMatchers("/payment/retrieve-paid-amount/{studentId}").hasAnyAuthority("ROLE_MODERATOR","ROLE_END_USER","ROLE_ADMINISTRATOR","ROLE_CORRESPONDENT")
                        .requestMatchers("/payment/create").hasAnyAuthority("ROLE_END_USER","ROLE_ADMINISTRATOR","ROLE_CORRESPONDENT")
                        .requestMatchers("/payment/retrieve-paid-amount/{studentId}").hasAnyAuthority("ROLE_MODERATOR","ROLE_END_USER","ROLE_ADMINISTRATOR","ROLE_CORRESPONDENT")
                        .requestMatchers("/payment/retrieve/{id}").hasAnyAuthority("ROLE_MODERATOR","ROLE_ADMINISTRATOR","ROLE_CORRESPONDENT")
                        .requestMatchers("/payment/**").hasAnyAuthority("ROLE_ADMINISTRATOR","ROLE_CORRESPONDENT")
                        .requestMatchers("/fees/student-id-fees-type").hasAnyAuthority("ROLE_END_USER","ROLE_MODERATOR","ROLE_ADMINISTRATOR","ROLE_CORRESPONDENT")
                        .requestMatchers("/fees/standard-all-fees-types").hasAnyAuthority("ROLE_MODERATOR","ROLE_ADMINISTRATOR","ROLE_CORRESPONDENT")
                        .requestMatchers("/fees/retrieve-all").hasAnyAuthority("ROLE_ADMINISTRATOR","ROLE_MODERATOR","ROLE_CORRESPONDENT")
                        .requestMatchers("/fees/retrieve/{id}").hasAnyAuthority("ROLE_ADMINISTRATOR","ROLE_MODERATOR","ROLE_CORRESPONDENT")
                        .requestMatchers("/fees/**").hasAnyAuthority("ROLE_ADMINISTRATOR","ROLE_CORRESPONDENT")
                        .requestMatchers("/scholar-ship/retrieve-student/{studentId}").hasAnyAuthority("ROLE_MODERATOR","ROLE_END_USER","ROLE_ADMINISTRATOR","ROLE_CORRESPONDENT")
                        .requestMatchers("/scholar-ship/retrieve/{id}").hasAnyAuthority("ROLE_ADMINISTRATOR","ROLE_MODERATOR","ROLE_CORRESPONDENT")
                        .requestMatchers("/scholar-ship/retrieve-all").hasAnyAuthority("ROLE_ADMINISTRATOR","ROLE_MODERATOR","ROLE_CORRESPONDENT")
                        .requestMatchers("/scholar-ship/**").hasAnyAuthority("ROLE_ADMINISTRATOR","ROLE_CORRESPONDENT")
                        .requestMatchers("/discount/retrieve-findDiscountByStudentId/{studentId}").hasAnyAuthority("ROLE_MODERATOR","ROLE_END_USER","ROLE_ADMINISTRATOR","ROLE_CORRESPONDENT")
                        .requestMatchers("/discount/retrieve/{id}").hasAnyAuthority("ROLE_ADMINISTRATOR","ROLE_MODERATOR","ROLE_CORRESPONDENT")
                        .requestMatchers("/discount/retrieve-all").hasAnyAuthority("ROLE_ADMINISTRATOR","ROLE_MODERATOR","ROLE_CORRESPONDENT")
                        .requestMatchers("/discount/**").hasAnyAuthority("ROLE_ADMINISTRATOR","ROLE_CORRESPONDENT"))
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(authenticationEntryPointImplement))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(this.jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
