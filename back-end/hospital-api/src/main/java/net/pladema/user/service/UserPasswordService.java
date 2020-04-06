package net.pladema.user.service;

import java.time.LocalDateTime;

import net.pladema.user.repository.entity.User;
import net.pladema.user.repository.entity.UserPassword;

public interface UserPasswordService {

	public UserPassword addPassword(User user, String password);
	
	public boolean validCredentials(String password, Integer userId);

	public void updatePassword(Integer userId, String password);

	public boolean passwordUpdated(Integer userId, LocalDateTime tokenExpirationDate);

}
