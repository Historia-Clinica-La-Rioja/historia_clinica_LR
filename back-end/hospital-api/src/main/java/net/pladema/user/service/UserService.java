package net.pladema.user.service;

import net.pladema.user.repository.entity.User;

import java.util.Optional;

public interface UserService {

	public User addUser(User user);

	void setEnable(User user, Boolean status);
	
	Optional<User> getUser(String username);
	
	public User getUser(Integer id);
	
	public Integer getUserId(String username);
	
	public boolean existUser(String username);
	
	public boolean existUser(Integer id);
	
	public void updateLoginDate(Integer userId);
	
	public boolean isEnable(String username); 
	
	public boolean isEnable(Integer id);

}
