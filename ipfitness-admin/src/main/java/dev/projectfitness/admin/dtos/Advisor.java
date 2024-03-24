package dev.projectfitness.admin.dtos;

public class Advisor {
	private Integer advisorId;
	private String displayName;
	private String username;
	private String password;
	private String email;
	private Integer adminId;

	public Advisor() {
		super();
	}

	public Advisor(Integer advisorId, String displayName, String username, String password, String email,
			Integer adminId) {
		super();
		this.advisorId = advisorId;
		this.displayName = displayName;
		this.username = username;
		this.password = password;
		this.email = email;
		this.adminId = adminId;
	}

	public Integer getAdvisorId() {
		return advisorId;
	}

	public void setAdvisorId(Integer advisorId) {
		this.advisorId = advisorId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

}
