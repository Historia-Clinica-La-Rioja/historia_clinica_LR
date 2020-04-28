package net.pladema.user.repository.entity;

public class UserBean {

	private UserBean() {
	}

	public static User newUser(String username) {
		User user = new User();
		user.setUsername(username);
		return user;
	}

	public static User newUser(Integer id, String username, Boolean enable) {
		User user = newUser(username);
		user.setId(id);
		user.setPersonId(id);
		user.setEnable(enable);
		return user;
	}
}
