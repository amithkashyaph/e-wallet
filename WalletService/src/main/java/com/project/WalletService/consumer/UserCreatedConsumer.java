package com.project.WalletService.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.Commons.UserIdentifier;
import com.project.WalletService.model.Wallet;
import com.project.WalletService.repository.WalletRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;

import com.project.Commons.Constants;
import org.springframework.stereotype.Service;


@Service
public class UserCreatedConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WalletRepository walletRepository;

//    @Value("${kafka.consumer.group.id}")
//    private String consumerGroupId;

    @KafkaListener(topics = Constants.USER_CREATION_TOPIC, groupId = "wallet-group")
    public void createWallet(String msg) throws JsonProcessingException {
        JSONObject jsonObject = objectMapper.readValue(msg, JSONObject.class);
        String contact = (String) jsonObject.get(Constants.USER_CREATION_TOPIC_CONTACT);
        String email = (String) jsonObject.get(Constants.USER_CREATION_TOPIC_EMAIL);
        Integer userId = (Integer) jsonObject.get(Constants.USER_CREATION_TOPIC_USER_ID);
        String userIdentifier = (String) jsonObject.get(Constants.USER_CREATION_TOPIC_USER_IDENTIFIER);
        String userIdentifierValue = (String) jsonObject.get(Constants.USER_CREATION_TOPIC_USER_IDENTIFIER_VALUE);

        Wallet wallet = Wallet.builder()
                .contact(contact)
                .userId(userId)
                .userIdentifier(UserIdentifier.valueOf(userIdentifier))
                .userIdentifierValue(userIdentifierValue)
                .balance(20.0)
                .build();

        UserIdentifier identifier = UserIdentifier.valueOf(userIdentifier);


        walletRepository.save(wallet);

    }
}
