package dev.projectfitness.advisor.beans;

import dev.projectfitness.advisor.dtos.Message;

public class MessageBean {
	private Message message;

	public MessageBean() {
		super();
	}

	public MessageBean(Message message) {
		super();
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

}
