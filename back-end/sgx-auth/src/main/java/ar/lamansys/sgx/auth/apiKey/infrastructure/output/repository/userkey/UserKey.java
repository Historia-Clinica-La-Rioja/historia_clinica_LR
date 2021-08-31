package ar.lamansys.sgx.auth.apiKey.infrastructure.output.repository.userkey;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_key")
@Getter
@Setter
@NoArgsConstructor
public class UserKey {

	/**
	 *
	 */
	private static final long serialVersionUID = -7104748562864239397L;

	@EmbeddedId
	private UserKeyPK pk;

	public UserKey(Integer userId, String key){
		pk = new UserKeyPK(userId, key);
	}

	public Integer getUserId(){
		return pk.getUserId();
	}
}
