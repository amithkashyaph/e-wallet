package com.project.NotificationService.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import com.project.Commons.Constants;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class UserCreatedConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SimpleMailMessage simpleMailMessage;

    @KafkaListener(topics = Constants.USER_CREATION_TOPIC, groupId = "notification-group")
    public void sendNotification(String msg) throws JsonProcessingException {
        JSONObject jsonObject = objectMapper.readValue(msg, JSONObject.class);
        String contact = (String) jsonObject.get(Constants.USER_CREATION_TOPIC_CONTACT);
        String email = (String) jsonObject.get(Constants.USER_CREATION_TOPIC_EMAIL);
        Integer userId = (Integer) jsonObject.get(Constants.USER_CREATION_TOPIC_USER_ID);
        String name = (String) jsonObject.get(Constants.USER_CREATION_TOPIC_USER_NAME);

        simpleMailMessage.setTo(email);
        simpleMailMessage.setText("Hello! Welcome "+ name + " to our e-wallet platform");
        simpleMailMessage.setSubject("Welcome mail");
        simpleMailMessage.setFrom("ewalletteam@gmail.com");

        javaMailSender.send(simpleMailMessage);
    }
}
