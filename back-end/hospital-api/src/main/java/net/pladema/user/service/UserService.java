package net.pladema.user.service;

import java.util.Optional;

import net.pladema.permissions.service.domain.UserBo;
import net.pladema.user.repository.entity.User;

public interface UserService {

	User addUser(User user);

	void setEnable(User user, Boolean status);
	
	Optional<User> getUser(String username);
	
	Integer getUserId(String username);
	
	void updateLoginDate(Integer userId);
	
	boolean isEnable(String username);

	Optional<UserBo> getUser(Integer userId);

}
