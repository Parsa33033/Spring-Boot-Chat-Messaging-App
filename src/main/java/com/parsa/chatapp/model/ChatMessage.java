package com.parsa.chatapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Id;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class ChatMessage {

	@PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
	private UUID id;
	private String sender;
	private String receiver;
	private String content;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
