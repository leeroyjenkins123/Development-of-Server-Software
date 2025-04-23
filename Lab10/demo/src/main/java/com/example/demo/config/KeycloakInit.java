package com.example.demo.config;

import com.example.demo.entity.customer.CustomerEntity;
import com.example.demo.entity.user.RoleEntity;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.repository.customer.CustomerRepository;
import com.example.demo.repository.user.RoleRepository;
import com.example.demo.repository.user.UserRepository;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

@Configuration
public class KeycloakInit {

    @Value("${keycloak.auth-service-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realmName;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Bean
    public Keycloak keycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master")
                .clientId("admin-cli")
                .username("admin")
                .password("admin")
                .build();
    }

    @Bean
    CommandLineRunner initKeycloak(Keycloak keycloak) {
        return args -> {
            boolean realmExists = keycloak.realms().findAll().stream()
                    .anyMatch(r -> r.getRealm().equals(realmName));

            if (realmExists) {
                keycloak.realms().realm(realmName).remove();
            }

            createRealm(keycloak);
        };
    }

    private void createRealm(Keycloak keycloak) {
        RealmRepresentation realm = new RealmRepresentation();
        realm.setRealm(realmName);
        realm.setEnabled(true);
        keycloak.realms().create(realm);

        ClientRepresentation client = new ClientRepresentation();
        client.setClientId(clientId);
        client.setEnabled(true);
        client.setDirectAccessGrantsEnabled(true);
        client.setPublicClient(true);
        client.setRedirectUris(List.of("http://localhost:8080/*"));
        keycloak.realm(realmName).clients().create(client);

        createRole(keycloak, realmName, "ROLE_USER");
        createRole(keycloak, realmName, "ROLE_ADMIN");

        createUser(keycloak, realmName, "user1", "useradmin", "ROLE_ADMIN");
        createUser(keycloak, realmName, "user2", "user", "ROLE_USER");

        RoleEntity role1 = roleRepository.findByRole("ROLE_ADMIN");
        RoleEntity role2 = roleRepository.findByRole("ROLE_USER");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("user1");
        userEntity.setPassword(passwordEncoder.encode("useradmin"));
        userEntity.setRole(role1);
        userRepository.save(userEntity);
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setUsername("user2");
        userEntity1.setPassword(passwordEncoder.encode("user"));
        CustomerEntity customer = customerRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        userEntity1.setCustomer(customer);
        userEntity1.setRole(role2);
        userRepository.save(userEntity1);
    }

    private void createRole(Keycloak keycloak, String realm, String roleName) {
        RoleRepresentation role = new RoleRepresentation();
        role.setName(roleName);
        keycloak.realm(realm).roles().create(role);
    }

    private void createUser(Keycloak keycloak, String realm, String username,
                            String password,String role)
    {
        UserRepresentation user = getUserRepresentation(username, password);

        keycloak.realm(realm).users().create(user);

        var userResource = keycloak.realm(realm).users()
                .get(keycloak.realm(realm).users().search(username).get(0).getId());

        RoleRepresentation roleRep = keycloak.realm(realm).roles().get(role).toRepresentation();
        userResource.roles().realmLevel().add(Collections.singletonList(roleRep));
    }

    private static UserRepresentation getUserRepresentation(String username, String password) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail("dummy@" + username + ".com");
        user.setFirstName("Default");
        user.setLastName("User");
        user.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        user.setCredentials(Collections.singletonList(credential));
        return user;
    }
}
