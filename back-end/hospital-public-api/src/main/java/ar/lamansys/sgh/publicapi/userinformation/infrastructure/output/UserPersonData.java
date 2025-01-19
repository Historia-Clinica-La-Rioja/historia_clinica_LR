package ar.lamansys.sgh.publicapi.userinformation.infrastructure.output;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "v_user_person_data")
public class UserPersonData {

	@Id
	private Integer id;

	@Column(name = "sub")
	private String sub;

	@Column(name = "email")
	private String email;

	@Column(name = "given_name")
	private String givenName;

	@Column(name = "family_name")
	private String familyName;

	@Column(name = "cuil")
	private String cuil;

	@Column(name = "identification_type")
	private String identificationType;

	@Column(name = "identification_number")
	private String identificationNumber;

	@Column(name = "gender")
	private String gender;
}
