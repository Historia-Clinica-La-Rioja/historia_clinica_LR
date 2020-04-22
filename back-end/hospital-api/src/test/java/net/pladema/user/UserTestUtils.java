package net.pladema.user;

import java.util.Random;

import net.pladema.user.repository.entity.User;
import net.pladema.user.repository.entity.UserPassword;

public class UserTestUtils {

    private UserTestUtils() {
    	
    }
	
    public static User createUser(String email) {
		User user = new User();
		user.setUsername(email);
		return user;
	}
    
    public static User getUserRandomId(String email) {
    	Random random = new Random();
    	User user = createUser(email);
    	user.setId(random.nextInt());
    	return user;
    }
       
    public static UserPassword createPassword(User user) {
    	UserPassword password = new UserPassword(user);
    	password.setPassword("PASSWORD");
    	password.setSalt("SALT");
    	password.setHashAlgorithm("HASHALGORITHM");
    	return password;    	
    }
//
//	public static AddUserDto addUserDtoValid() {
//		AddUserDto response = new AddUserDto();
//		response.setPassword("PASSWORD");
//		response.setEmail("EMAIL@GMAIL.COM");
//		return response;
//	}
//
//	public static AbstractUserDto addUserDtoInvalidEmail() {
//		AddUserDto response = addUserDtoValid();
//		response.setEmail("BadEMail");
//		return response;
//	}
//
//	public static AddUserDto addUserDtoInvalid() {
//		AddUserDto response = addUserDtoValid();
//		response.setPassword(null);
//		response.setEmail("BadEMail");
//		return response;
//	}
}
