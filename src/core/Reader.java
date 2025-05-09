package core;

public class Reader {
	private Integer id;
	private String username;
	private String mail;
	private String password;
	
	public Reader(int id, String username, String mail, String password) {
		this.id = id;
		this.username = username;
		this.mail = mail;
		this.password = password;
	}
	
	public Reader(String username, String mail, String password) {
		this.username = username;
		this.mail = mail;
		this.password = password;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
