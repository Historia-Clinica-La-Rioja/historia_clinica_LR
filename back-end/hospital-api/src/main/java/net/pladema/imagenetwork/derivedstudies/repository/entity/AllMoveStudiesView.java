package net.pladema.imagenetwork.derivedstudies.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDate;

@Entity
@Table(name = "v_all_move_studies")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AllMoveStudiesView implements Serializable {

	@Id
	@Column(name = "id_move")
	private Integer id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "identification_number")
	private String identificationNumber;

	@Column(name = "image_id")
	private String imageId;

	@Column(name = "appoinment_date")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate appoinmentDate;

	@Column(name = "appoinment_time")
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	private Time appoinmentTime;

	@Column(name = "status")
	private String status;

	@Column(name = "result")
	private String result;

	@Column(name = "institution_id", nullable = true)
	private Integer institutionId;

	@Column(name = "acronym")
	private String acronym;



}
