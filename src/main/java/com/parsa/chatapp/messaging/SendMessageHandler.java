package com.parsa.chatapp.messaging;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.parsa.chatapp.config.RabbitConfig;
import com.parsa.chatapp.model.InstantChatMessage;

@Component
public class SendMessageHandler {

	@Autowired
	RabbitMessagingTemplate rabbitMessagingTemplate;

	@Autowired
	AmqpAdmin amqpAdmin;

	public void createExchange(String username) {
		DirectExchange exchange = new DirectExchange(username + "_exchange");
		amqpAdmin.declareExchange(exchange);
		Binding binding = new Binding(exchange.getName(), DestinationType.EXCHANGE, RabbitConfig.INCOMING_EXCHANGE,
				username + ".#", null);
		amqpAdmin.declareBinding(binding);
	}

	public void sendMessageToOutgoingExchange(InstantChatMessage payload) {
		rabbitMessagingTemplate.convertAndSend(RabbitConfig.OUTGOING_EXCHANGE, "x", payload);
	}

}
