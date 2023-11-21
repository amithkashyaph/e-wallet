package com.project.UserService.configuration;

import com.project.UserService.service.UserService;
import org.apache.kafka.common.config.types.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Value("${admin.authority}")
    private String adminAuthority;

    @Value("${user.authority}")
    private String userAuthority;

    //Authentication
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(this.userService);
    }

    // Authorization
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable().httpBasic().and().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/user/create").permitAll()
                .antMatchers("/user/details/me").hasAuthority(userAuthority)
                .antMatchers("/user/details").permitAll()
                .antMatchers("/**").permitAll();
    }
 }
