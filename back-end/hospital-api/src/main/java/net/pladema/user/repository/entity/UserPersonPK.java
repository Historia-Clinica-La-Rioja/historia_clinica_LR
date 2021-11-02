package net.pladema.user.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserPersonPK implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Column(name = "user_id", nullable = false)
	private Integer userId;

	@Column(name = "person_id", nullable = false)
	private Integer personId;


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserPersonPK that = (UserPersonPK) o;
		return Objects.equals(userId, that.userId) && Objects.equals(personId, that.personId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, personId);
	}
}
