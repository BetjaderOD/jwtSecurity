package com.utez.sda.springjwt.config;

import com.utez.sda.springjwt.filter.JwtAuthFilter;
import com.utez.sda.springjwt.service.UserInfoDetails;
import com.utez.sda.springjwt.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
   private JwtAuthFilter authFilter;

    @Bean
    public UserDetailsService userDetailsService(){
        return  new UserInfoService();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
        throws Exception{
            return  configuration.getAuthenticationManager();
        }
        @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws   Exception{
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests((request)->{
            request.requestMatchers("/auth/login").permitAll();
            request.requestMatchers("/auth/index").permitAll();

            request.requestMatchers("/auth/registrame").permitAll();
            request.requestMatchers("/auth/admin/test").permitAll();
            request.requestMatchers("/auth/user/test").permitAll();


        });
            http.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
            http.authenticationProvider(authenticationProvider());
            http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
        }

        @Bean
        public AuthenticationProvider authenticationProvider(){
            DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
            authenticationProvider.setUserDetailsService(
                    userDetailsService()
            );
            authenticationProvider.setPasswordEncoder(passwordEncoder());
            return  authenticationProvider;
        }




}
