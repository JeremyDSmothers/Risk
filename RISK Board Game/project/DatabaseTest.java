package project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class DatabaseTest {

	private String[] users = { "antone", "edgar", "jeremy", "sarah", "matthew" };
	private String[] passwords = { "passworda", "passworde", "passwordj", "passworde", "passwordm" };
	private Database db;
	private int random;

	@Before
	public void setup() {
		db = new Database();
		random = (int) (Math.random() * users.length);
	}

	@Test
	public void testSetConnection() {
		db.setConnection("project//db.properties");
		assertNotNull("Connection object was null", db.getConnection());
	}

	@Test
	public void testQuery() throws SQLException {
		db.setConnection("project//db.properties");
		String query = String.format("select aes_decrypt(password,'key') from user where username = '%s';",
				users[random]);
		ArrayList<String> result = db.query(query);
		assertEquals(result.get(0), passwords[random]);
	}

	@Test
	public void testQueryEmpty() throws SQLException {
		db.setConnection("project//db.properties");
		String query = String.format("select aes_decrypt(password,'key') from user where username = '%s';", "123");
		ArrayList<String> result = db.query(query);
		try {
			result.get(0);
			assertTrue(false);
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testExecuteDMLSuccess() {
		db.setConnection("project//db.properties");
		String query = "INSERT INTO user VALUES('test',aes_encrypt('d123456','key'),0,0);";
		try {
			db.executeDML(query);
			assertTrue(true);
		} catch (SQLException e) {
			assertTrue(false);
		}
	}

	@Test
	public void testExecuteDMLFailure() {
		db.setConnection("project//db.properties");
		String query = "INSERT INTO user VALUES('matthew',aes_encrypt('d123456','key'),0,0);";
		try {
			db.executeDML(query);
			assertTrue(false);
		} catch (SQLException e) {
			assertTrue(true);
		}
	}
}
