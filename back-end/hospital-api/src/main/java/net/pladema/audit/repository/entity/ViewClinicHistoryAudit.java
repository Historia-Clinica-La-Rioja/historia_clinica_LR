package net.pladema.audit.repository.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.format.annotation.DateTimeFormat;

@Setter
@Getter
@NoArgsConstructor
@Table(name = "v_clinic_history_audit")
@Entity
public class ViewClinicHistoryAudit {

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "name_self_determination")
	private String nameSelfDetermination ;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "identification_number", nullable = false)
	private String identificationNumber;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "date", nullable = false)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate date;
	@Column(name = "observations", columnDefinition = "TEXT")
	private String observations;

	@Column(name = "name", nullable = false)
	private String institutionName;

	@Column(name = "reason_id", nullable = false)
	private Short reasonId;

	@Column(name = "scope")
	private Short scope;

}
