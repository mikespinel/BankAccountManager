package com.aitho.contocorrente;

import com.aitho.contocorrente.model.Customer;
import com.aitho.contocorrente.model.Role;
import com.aitho.contocorrente.enums.RoleEnum;
import com.aitho.contocorrente.repository.RoleRepository;
import com.aitho.contocorrente.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class ContocorrenteApplication {

	@Autowired
	RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(ContocorrenteApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(CustomerService customerService) {
		return args -> {
			Customer michael = customerService.findById(1L).get();
			michael.setPassword("1234");
			michael.setEmail("michael@email.com");
			Set<Role> roles = new HashSet<>();
			Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
			michael.setRoles(roles);
			customerService.save(michael);

		};
	}

}
