package dev.projectfitness.advisor.dtos;

public class Advisor {
	private Integer advisorId;
	private String username;
	private String password;
	private String displayName;

	public Advisor() {
		super();
	}

	public Advisor(Integer advisorId, String username, String password, String displayName) {
		super();
		this.advisorId = advisorId;
		this.username = username;
		this.password = password;
		this.displayName = displayName;
	}

	public Integer getAdvisorId() {
		return advisorId;
	}

	public void setAdvisorId(Integer advisorId) {
		this.advisorId = advisorId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
