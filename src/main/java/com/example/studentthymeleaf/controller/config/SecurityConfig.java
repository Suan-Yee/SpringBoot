package com.example.studentthymeleaf.controller.config;

import com.example.studentthymeleaf.entity.User;
import com.example.studentthymeleaf.exception.CustomAccessDeniedHandler;
import com.example.studentthymeleaf.exception.CustomAuthenticationEntryPoint;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

//    private final BCryptPasswordEncoder encoder;
//    private final CustomAccessDeniedHandler customAccessDeniedHandler;
//    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
//    private final UserDetailsService userDetailsService;

//    private static final String[] URLS = {"/**"};
//
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        /*http.formLogin(log -> log.loginPage("/welcome").usernameParameter("email").loginProcessingUrl("/login").defaultSuccessUrl("/"));*/
//        http.csrf(csrf -> csrf.disable());
//        http.authorizeHttpRequests(request -> request.requestMatchers(URLS).permitAll());
//        http.exceptionHandling(exception -> exception.accessDeniedHandler(customAccessDeniedHandler).authenticationEntryPoint(customAuthenticationEntryPoint));
//        http.authorizeHttpRequests(auth -> auth
//                .requestMatchers("/resources/").permitAll()
//                .requestMatchers("/static/css").permitAll()
//                .anyRequest().authenticated());
//        return http.build();
//    }
//
//    @Bean
//    AuthenticationManager authenticationManager(){
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService);
//        authenticationProvider.setPasswordEncoder(encoder);
//        return new ProviderManager(authenticationProvider);
//    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(log -> log.loginPage("/login").usernameParameter("email").loginProcessingUrl("/signIn").defaultSuccessUrl("/"));

        http.logout(logout ->  logout.invalidateHttpSession(true).clearAuthentication(true).logoutUrl("/logout").logoutSuccessUrl("/"));
/*
        http.logout(form -> form.invalidateHttpSession(true).clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/login"))
                .logoutSuccessUrl("/").permitAll());*/

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login","/static/**","/user/register/**").permitAll()
                .requestMatchers("/user/**").hasAnyAuthority(User.Role.USER.name(), User.Role.ADMIN.name())
                .requestMatchers("/admin/**").hasAnyAuthority(User.Role.ADMIN.name())
                .anyRequest().authenticated());
        return http.build();
    }

}
