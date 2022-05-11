package eus.natureops.natureops;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import eus.natureops.natureops.domain.Role;
import eus.natureops.natureops.domain.User;
import eus.natureops.natureops.service.RoleService;
import eus.natureops.natureops.service.UserService;

@SpringBootApplication
public class NatureopsApplication {

	public static void main(String[] args) {
		SpringApplication.run(NatureopsApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedHeaders("*").allowedMethods("*").allowedOrigins("http://localhost:3000");
			}
		};
	}

	@Bean
	CommandLineRunner run(UserService userService, RoleService roleService) {
		return args -> {
			roleService.save(new Role(1L, "ROLE_USER", true, 1));
			roleService.save(new Role(2L, "ROLE_ONG", true, 1));
			roleService.save(new Role(3L, "ROLE_PO", true, 1));

			userService.register(new User(1L, "eka", "123", "Eka", "User", "ekaitz@email.com", null, true, null, 1));
			userService.setRole("eka", "ROLE_USER");
		};
	}
}
