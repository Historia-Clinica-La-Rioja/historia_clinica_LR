package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "newborn")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Newborn implements Serializable {

	private static final long serialVersionUID = -3053291021636483828L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "weight", nullable = false)
	private Short weight;

	@Column(name = "birth_condition_type", nullable = false)
	private Short birthConditionType;

	@Column(name = "gender_id", nullable = false)
	private Short genderId;

	@Column(name = "obstetric_event_id", nullable = false)
	private Integer obstetricEventId;

}
