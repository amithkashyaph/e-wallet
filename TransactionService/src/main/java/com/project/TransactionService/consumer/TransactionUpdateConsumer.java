package com.project.TransactionService.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.Commons.Constants;
import com.project.TransactionService.model.enums.TransactionStatus;
import com.project.TransactionService.repository.TransactionRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class TransactionUpdateConsumer {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = Constants.TRANSACTION_UPDATION_TOPIC, groupId = "transaction-group")
    public void processTransactionUpdate(String msg) throws JsonProcessingException {
        JSONObject object = objectMapper.readValue(msg, JSONObject.class);

        String status = (String) object.get("status");
        String txnId = (String) object.get("txnId");

        transactionRepository.updateTxnStatus(txnId, TransactionStatus.valueOf(status));
    }
}
