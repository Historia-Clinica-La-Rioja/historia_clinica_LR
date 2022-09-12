package net.pladema.staff.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "v_available_professional")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class VAvailableProfessional implements Serializable {

	private static final long serialVersionUID = 7788785116152807155L;

	@Id
	@Column(name = "id")
	@EqualsAndHashCode.Include
	@ToString.Include
	private Integer id;

	@Column(name = "person_id", nullable = false, unique = true)
	@ToString.Include
	private Integer personId;

	@Column(name = "first_name", length = 40)
	@ToString.Include
	private String firstName;

	@Column(name = "last_name", length = 40)
	@ToString.Include
	private String lastName;

	@Column(name = "identification_type_id")
	@ToString.Include
	private Short identificationTypeId;

	@Column(name = "identification_number", length = 11)
	@ToString.Include
	private String identificationNumber;


}
