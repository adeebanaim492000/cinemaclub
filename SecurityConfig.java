package com.cinemaclub.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/movies/*", "/api/**", "/css/**", "/js/**").permitAll()
                    .antMatchers("/movies/new").hasRole("ADMIN")
                    .antMatchers("/movies/*/reviews").hasAnyRole("USER", "ADMIN")
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                        .loginPage("/login").permitAll()
                        .defaultSuccessUrl("/", true)
                .and()
                    .logout()
                        .logoutSuccessUrl("/")
                        .permitAll()
                .and()
                    .csrf();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                    .password("{noop}password")
                    .roles("USER")
                .and()
                .withUser("admin")
                    .password("{noop}password")
                    .roles("ADMIN");
    }
}

