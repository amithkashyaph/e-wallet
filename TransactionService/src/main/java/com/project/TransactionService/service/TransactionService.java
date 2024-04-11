package com.project.TransactionService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.Commons.Constants;
import com.project.TransactionService.model.Transaction;
import com.project.TransactionService.model.enums.TransactionStatus;
import com.project.TransactionService.repository.TransactionRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class TransactionService implements UserDetailsService  {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${user.service.host}")
    private String userServiceHost;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity request = new HttpEntity(httpHeaders);

        JSONObject userObject = restTemplate.exchange(userServiceHost + username,
                HttpMethod.GET, request, JSONObject.class).getBody();

        List<LinkedHashMap<String, String>> authorities = (List<LinkedHashMap<String, String>>) userObject.get("authorities");
        List<GrantedAuthority> authorityList = authorities.stream().
                map(a -> a.get("authority")).
                map(s -> new SimpleGrantedAuthority(s)).
                collect(Collectors.toList());

        User user = new User((String) userObject.get("username"), (String) userObject.get("password"), authorityList);
        return user;
    }

    public String initiateTransaction(String username, String receiver, String amount, String purpose) throws JsonProcessingException {
        Transaction transaction = Transaction.builder().sender(username)
                .receiver(receiver)
                .transactionStatus(TransactionStatus.INITIATED)
                .amount(Double.valueOf(amount))
                .txnId(UUID.randomUUID().toString())
                .build();

        transactionRepository.save(transaction);

        // Publish an event to Kafka after successful transaction record creation
        // WalletService will be a consumer of this event and will carry out the debit/credit actions accordingly
        JSONObject txnObject = new JSONObject();
        txnObject.put("amount", transaction.getAmount());
        txnObject.put("sender", transaction.getSender());
        txnObject.put("receiver", transaction.getReceiver());
        txnObject.put("txnId", transaction.getTxnId());

        kafkaTemplate.send(Constants.TRANSACTION_INITIATION_TOPIC, objectMapper.writeValueAsString(txnObject));

        return "Transaction successfully initiated";
    }
}
