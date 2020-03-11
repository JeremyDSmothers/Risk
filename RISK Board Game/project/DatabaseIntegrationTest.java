package project;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import project.CreateAccount.CreateAccountData;
import project.Login.LoginData;

public class DatabaseIntegrationTest {

	private String[] users = { "antone", "edgar", "jeremy", "sarah", "matthew" };
	private String[] passwords = { "passworda", "passworde", "passwordj", "passworde", "passwordm" };
	private GameClient client;
	private Database db;
	private LoginData data;
	private CreateAccountData cdata;
	private int random;

	@Before
	public void setup() {
		int port = 8635;
		data = new LoginData();
		cdata = new CreateAccountData();
		random = (int) (Math.random() * users.length);
		client = new GameClient("localhost", 9300);
		db = new Database();
	}

	@Test
	public void loginVerifySuccessful() {
		db.setConnection("project//db.properties");
		data.setUsername(users[random]);
		data.setPassword(passwords[random]);

		String query = "SELECT UserName FROM user WHERE UserName = '" + data.getUsername()
				+ "' AND aes_decrypt(password,'key') = '" + data.getPassword() + "';";
		ArrayList<String> result = db.query(query);

		if ((result.get(0)).equals(data.getUsername()))
			client.setVerifyCredentials(true);
		else
			client.setVerifyCredentials(false);

		if (client.getVerifyCredentials())
			assertTrue(true);
		else
			assertTrue(false);
	}

	@Test
	public void loginVerifyFailed() {
		db.setConnection("project//db.properties");
		data.setUsername("123");
		data.setPassword(passwords[random]);

		String query = "SELECT UserName FROM user WHERE UserName = '" + data.getUsername()
				+ "' AND aes_decrypt(password,'key') = '" + data.getPassword() + "';";
		ArrayList<String> result = db.query(query);

		try {
			if (result.get(0).equals(data.getUsername()))
				client.setVerifyCredentials(false);
		} catch (IndexOutOfBoundsException e) {
			client.setVerifyCredentials(true);
		}

		if (client.getVerifyCredentials())
			assertTrue(true);
		else
			assertTrue(false);
	}

	@Test
	public void createAccountSuccessful() {
		db.setConnection("project//db.properties");
		cdata.setUsername("test123");
		cdata.setPassword(passwords[random]);

		String query = "INSERT INTO user VALUES('" + cdata.getUsername() + "','" + cdata.getPassword() + "',0,0);";

		try {
			db.executeDML(query);
			client.setVerifyCredentials(true);
		} catch (SQLException e) {
			client.setVerifyCredentials(false);
		}

		if (client.getVerifyCredentials())
			assertTrue(true);
		else
			assertTrue(false);

		try {
			db.executeDML("DELETE FROM user WHERE username = 'test123'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void createAccountFail() {
		db.setConnection("project//db.properties");
		cdata.setUsername(users[random]);
		cdata.setPassword(passwords[random]);

		String query = "INSERT INTO user VALUES('" + cdata.getUsername() + "','" + cdata.getPassword() + "',0,0);";

		try {
			db.executeDML(query);
			client.setVerifyCredentials(false);
		} catch (SQLException e) {
			client.setVerifyCredentials(true);
		}

		if (client.getVerifyCredentials())
			assertTrue(true);
		else
			assertTrue(false);
	}

	@After
	public void cleanup() throws IOException {
		client.closeConnection();
	}
}