package net.pladema.person.repository.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "person_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonHistory implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -8310665281279550155L;


	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "person_id", length = 40)
	private Integer personId;

	@Column(name = "first_name", length = 40)
	private String firstName;

	@Column(name = "middle_names", length = 40)
	private String middleNames;

	@Column(name = "last_name", length = 40)
	private String lastName;

	@Column(name = "other_last_names", length = 40)
	private String otherLastNames;

	@Column(name = "identification_type_id")
	private Short identificationTypeId;

	@Column(name = "identification_number", length = 11)
	private String identificationNumber;

	@Column(name = "gender_id")
	private Short genderId;

	@Column(name = "birth_date")
	private LocalDate birthDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_on")
	private LocalDateTime createdOn;

	public PersonHistory(Person p) {
		this.personId = p.getId();
		this.firstName = p.getFirstName();
		this.middleNames = p.getMiddleNames();
		this.lastName = p.getLastName();
		this.otherLastNames = p.getOtherLastNames();
		this.identificationTypeId = p.getIdentificationTypeId();
		this.identificationNumber = p.getIdentificationNumber();
		this.genderId = p.getGenderId();
		this.birthDate = p.getBirthDate();
		this.createdOn = LocalDateTime.now();
		this.createdBy = SecurityContextUtils.getUserDetails().userId;
	}
}
