package com.parsa.chatapp.messaging;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.parsa.chatapp.config.RabbitConfig;
import com.parsa.chatapp.controller.MessagingController;
import com.parsa.chatapp.model.ChatMessage;
import com.parsa.chatapp.model.InstantChatMessage;
import com.parsa.chatapp.repo.ChatMessageRepository;
import com.parsa.chatapp.repo.OnlineUserRepository;

@Service
public class ReceiveMessageListener {

	private final static String LISTENER_CONTAINER = "queue_listener_container";
	private Logger logger = LoggerFactory.getLogger(MessagingController.class);

	@Autowired
	AmqpAdmin amqpAdmin;

	@Autowired
	RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

	@Autowired
	ChatMessageRepository chatMessageRepo;

	@Autowired
	RabbitMessagingTemplate rabbitMessagingTemplate;

	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	OnlineUserRepository onlineUserRepo;

	@RabbitListener(queues = { RabbitConfig.CHAT_MESSAGING_QUEUE })
	public void listenToChatMessagingQueue(InstantChatMessage payload) {
		String receiver = payload.getReceiver();
		String sender = payload.getSender();
		logger.info("---->>>" + sender);
		// create queue
		createMessageSenderQueue(sender, receiver);

		// save message
		ChatMessage msg = new ChatMessage();
		msg.setContent(payload.getContent());
		msg.setReceiver(receiver);
		msg.setSender(sender);
		Date date = new Date();
		String uuid = payload.getSender() + "_" + payload.getReceiver() + "_" + date.toString();
		msg.setId(UUID.nameUUIDFromBytes(uuid.getBytes()));
		chatMessageRepo.save(msg);

		// send message
		rabbitMessagingTemplate.convertAndSend(RabbitConfig.INCOMING_EXCHANGE, receiver + "." + sender, payload);
	}

	public void createMessageSenderQueue(String sender, String receiver) {
		Queue queue = new Queue(sender + "_queue", true, false, false);
		amqpAdmin.declareQueue(queue);
		SimpleMessageListenerContainer container = (SimpleMessageListenerContainer) rabbitListenerEndpointRegistry
				.getListenerContainer(LISTENER_CONTAINER);
		container.addQueueNames(queue.getName());
		Binding binding = new Binding(queue.getName(), DestinationType.QUEUE, receiver + "_exchange",
				receiver + "." + sender, null);
		amqpAdmin.declareBinding(binding);
	}

	@RabbitListener(id = LISTENER_CONTAINER)
	public void senderQueueListener(InstantChatMessage payload) {
		simpMessagingTemplate.convertAndSend("/topic/" + payload.getReceiver() + "/reply", payload);
	}

	public void disconnectListener(String sessionId) {
		onlineUserRepo.deleteById(sessionId);
		logger.warn("disconnected............." + sessionId);
	}
}
