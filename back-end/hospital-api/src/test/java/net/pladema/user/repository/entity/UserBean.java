package net.pladema.user.repository.entity;

public class UserBean {

	private UserBean() {
	}

	public static User newUser(Integer id, String username, Boolean enable) {
		User user = new User();
		user.setId(id);
		user.setPersonId(id);
		user.setUsername(username);
		user.setEnable(enable);
		return user;
	}
}
