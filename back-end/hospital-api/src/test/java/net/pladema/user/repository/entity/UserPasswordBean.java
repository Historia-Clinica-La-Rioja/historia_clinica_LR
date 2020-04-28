package net.pladema.user.repository.entity;

public class UserPasswordBean {

	private UserPasswordBean() {
	}

	public static UserPassword newUserPassword(Integer userId) {
		UserPassword password = new UserPassword();
		password.setId(userId);
		password.setPassword("PASSWORD");
		password.setSalt("SALT");
		password.setHashAlgorithm("HASHALGORITHM");
		return password;
	}

}
