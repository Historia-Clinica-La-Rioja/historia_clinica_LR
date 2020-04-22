package net.pladema.user.service;

import java.time.LocalDateTime;

import net.pladema.user.repository.entity.User;
import net.pladema.user.repository.entity.UserPassword;

public interface UserPasswordService {

	UserPassword addPassword(User user, String password);
	
	boolean validCredentials(String password, Integer userId);

	void updatePassword(Integer userId, String password);

	boolean passwordUpdated(Integer userId, LocalDateTime tokenExpirationDate);

	void setPassword(Integer userId, String password);
}
