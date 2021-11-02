package net.pladema.user.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_person")
@Getter
@Setter
@NoArgsConstructor
public class UserPerson {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@EmbeddedId
	private UserPersonPK pk;

	public UserPerson(Integer userId, Integer personId){
		pk = new UserPersonPK(userId, personId);
	}

	public Integer getUserId(){
		return pk.getUserId();
	}

	public Integer getPersonId() {
		return pk.getPersonId();
	}
}
