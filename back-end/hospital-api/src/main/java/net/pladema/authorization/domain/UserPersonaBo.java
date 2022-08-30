package net.pladema.authorization.domain;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public class UserPersonaBo {

	public final String email;
	public final Integer id;
	public final Integer personId;
	public final String firstName;
	public final String lastName;
	public final Supplier<String> avatar;
	public final String nameSelfDetermination;
	public final LocalDateTime previousLogin;

	public UserPersonaBo(String email, Integer id, Integer personId, String firstName, String lastName, Supplier<String> avatar, String nameSelfDetermination, LocalDateTime previousLogin) {
		this.email = email;
		this.id = id;
		this.personId = personId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.avatar = avatar;
		this.nameSelfDetermination = nameSelfDetermination;
		this.previousLogin = previousLogin;
	}

}
