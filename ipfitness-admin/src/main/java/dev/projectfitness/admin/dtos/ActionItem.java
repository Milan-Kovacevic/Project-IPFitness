package dev.projectfitness.admin.dtos;

public class ActionItem {
	private Integer actionId;
	private String ipAddress;
	private String endpoint;
	private String message;
	private Integer severity;
	private String actionTime;

	public ActionItem() {
		super();
	}

	public ActionItem(Integer actionId, String ipAddress, String endpoint, String message, Integer severity,
			String actionTime) {
		super();
		this.actionId = actionId;
		this.ipAddress = ipAddress;
		this.endpoint = endpoint;
		this.message = message;
		this.severity = severity;
		this.actionTime = actionTime;
	}

	public Integer getActionId() {
		return actionId;
	}

	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getSeverity() {
		return severity;
	}

	public void setSeverity(Integer severity) {
		this.severity = severity;
	}

	public String getActionTime() {
		return actionTime;
	}

	public void setActionTime(String actionTime) {
		this.actionTime = actionTime;
	}

}
