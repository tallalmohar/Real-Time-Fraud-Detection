package com.fraud.producer.service;

import com.fraud.common.model.Transaction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTransactionProducer {
    private final TransactionGenerator transactionGenerator;
    private final KafkaProducerService kafkaProducerService;

    public ScheduledTransactionProducer(TransactionGenerator tG, KafkaProducerService kS){
        this.transactionGenerator = tG;
        this.kafkaProducerService = kS;
    }

    @Scheduled(fixedRate = 2000)
    public void produce(){
        Transaction transaction = transactionGenerator.generateTransaction();
        kafkaProducerService.sendTransaction(transaction);
    }


}
