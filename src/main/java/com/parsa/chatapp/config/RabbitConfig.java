package com.parsa.chatapp.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

	public final static String CHAT_MESSAGING_QUEUE = "chat_messaging_queue";
	public final static String INCOMING_EXCHANGE = "incoming_exchange";
	public final static String OUTGOING_EXCHANGE = "outgoing_exchange";

	@Bean
	public Queue createChatMessagingQueue() {
		return QueueBuilder.durable(CHAT_MESSAGING_QUEUE).build();
	}

	@Bean
	public TopicExchange createIncomingExchange() {
		return ExchangeBuilder.topicExchange(INCOMING_EXCHANGE).build();
	}

	@Bean
	public FanoutExchange createOutgoingExchange() {
		return ExchangeBuilder.fanoutExchange(OUTGOING_EXCHANGE).build();
	}

	@Bean
	public Binding createChatMessagingBinding() {
		return BindingBuilder.bind(createChatMessagingQueue()).to(createOutgoingExchange());
	}

}
