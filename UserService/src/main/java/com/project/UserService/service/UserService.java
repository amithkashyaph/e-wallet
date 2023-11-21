package com.project.UserService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.UserService.dto.CreateUserRequestDTO;
import com.project.UserService.model.User;
import com.project.UserService.repository.UserRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.project.Commons.Constants;

import java.net.http.HttpHeaders;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${user.authority}")
    private String userAuthority;

    @Value("${kafka.user.created.topic}")
    private String userCreatedTopic;

    public User createUser(CreateUserRequestDTO createUserRequestDTO) throws JsonProcessingException {
        User user = createUserRequestDTO.toUser();
        user.setPassword(passwordEncoder.encode(createUserRequestDTO.getPassword()));
        user.setAuthorities(userAuthority);

        user = userRepository.save(user);

        // Produce events for wallet service and notification service on successful user creation to :
        // 1. Initialize wallet for the newly created user
        // 2. Send notification to the user to inform him that his account has been successfully created

        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put(Constants.USER_CREATION_TOPIC_USER_NAME, user.getName());
        userJsonObject.put(Constants.USER_CREATION_TOPIC_EMAIL, user.getEmail());
        userJsonObject.put(Constants.USER_CREATION_TOPIC_CONTACT, user.getContact());
        userJsonObject.put(Constants.USER_CREATION_TOPIC_USER_IDENTIFIER, user.getUserIdentifier());
        userJsonObject.put(Constants.USER_CREATION_TOPIC_USER_IDENTIFIER_VALUE, user.getUserIdentifierValue());
        userJsonObject.put(Constants.USER_CREATION_TOPIC_USER_ID, user.getId());

        kafkaTemplate.send(Constants.USER_CREATION_TOPIC, objectMapper.writeValueAsString(userJsonObject));

        return user;

    }

    @Override
    public User loadUserByUsername(String contact) throws UsernameNotFoundException {
        return userRepository.findByContact(contact);
    }
}
