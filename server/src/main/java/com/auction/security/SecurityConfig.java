package com.auction.security;

import com.auction.db.UserRepo;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

//@EnableWebSecurity
@EnableJpaRepositories(basePackageClasses = UserRepo.class)
@Configuration
public class SecurityConfig {
}
