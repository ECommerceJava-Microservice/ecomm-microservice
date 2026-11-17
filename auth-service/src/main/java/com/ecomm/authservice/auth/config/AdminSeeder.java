package com.ecomm.authservice.auth.config;

import java.util.Arrays;
import com.ecomm.authservice.auth.entity.AppRole;
import com.ecomm.authservice.auth.entity.Role;
import com.ecomm.authservice.auth.entity.User;
import com.ecomm.authservice.auth.repository.RoleRepository;
import com.ecomm.authservice.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email:}")
    private String adminEmail;

    @Value("${app.admin.password:}")
    private String adminPassword;

    @Override
    @Transactional
    public void run(String... args) {
        seedRoles();
        seedAdmin();
    }

    private void seedRoles() {
        Arrays.stream(AppRole.values())
                .forEach(role -> roleRepository.findByRoleName(role)
                        .orElseGet(() -> roleRepository.save(Role.builder().roleName(role).build())));
    }

    private void seedAdmin() {
        if (adminEmail.isBlank() || adminPassword.isBlank()) {
            log.info("app.admin.email/password təyin olunmayıb, admin seed edilmir.");
            return;
        }

        boolean adminExists = userRepository.findByEmail(adminEmail).isPresent();
        if (adminExists) {
            return;
        }

        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN tapılmadı"));

        User admin = User.builder()
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .enabled(true)
                .roles(Set.of(adminRole))
                .build();

        userRepository.save(admin);
        log.info("İlk admin yaradıldı: {}", adminEmail);
    }
}
