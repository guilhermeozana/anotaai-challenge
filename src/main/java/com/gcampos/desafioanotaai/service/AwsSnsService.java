package com.gcampos.desafioanotaai.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.Topic;
import com.gcampos.desafioanotaai.domain.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class AwsSnsService {
    private final AmazonSNS snsClient;

    @Qualifier("catalogEventsTopic")
    private final Topic catalogTopic;


    public void publish(MessageDTO message) {
        System.out.println(message.message());
        this.snsClient.publish(catalogTopic.getTopicArn(), message.message());
    }
}