package project.CreateAccount;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CreateAccountData implements Serializable {

	private String username;
	private String password;
	private String verify;

	public CreateAccountData() {
		this.username = "No Username";
		this.password = "No Password";
		this.verify = "No Password";
	}

	public CreateAccountData(String username, String password, String verify) {
		this.username = username;
		this.password = password;
		this.verify = verify;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getVerify() {
		return verify;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}
}
