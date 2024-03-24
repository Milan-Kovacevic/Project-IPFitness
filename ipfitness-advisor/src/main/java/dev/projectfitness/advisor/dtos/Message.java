package dev.projectfitness.advisor.dtos;

public class Message {
	private Integer questionId;
	private String content;
	private String senderName;
	private String senderMail;
	private String sendDate;

	public Message() {
		super();
	}

	public Message(Integer questionId, String content, String senderName, String senderMail, String sendDate) {
		super();
		this.questionId = questionId;
		this.content = content;
		this.senderName = senderName;
		this.senderMail = senderMail;
		this.sendDate = sendDate;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderMail() {
		return senderMail;
	}

	public void setSenderMail(String senderMail) {
		this.senderMail = senderMail;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

}
