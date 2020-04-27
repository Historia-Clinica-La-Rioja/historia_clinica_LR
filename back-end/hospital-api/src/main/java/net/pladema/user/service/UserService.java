package net.pladema.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.pladema.user.repository.entity.User;
import net.pladema.user.repository.projections.PageableUsers;

import java.util.Optional;

public interface UserService {

	public User addUser(User user);

	public User updateUser(User user);
	
	Optional<User> getUser(String username);
	
	public User getUser(Integer id);
	
	public Integer getUserId(String username);
	
	public boolean existUser(String username);
	
	public boolean existUser(Integer id);
	
	public void changeStatusAccount(Integer userId, Boolean status);
	
	public void updateLoginDate(Integer userId);
	
	public boolean isEnable(String username); 
	
	public boolean isEnable(Integer id);

	public Page<PageableUsers> pegeableUsers(Pageable pageable);

}
