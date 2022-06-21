package net.pladema.clinichistory.hospitalization.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "discharge_type")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DischargeType implements Serializable {

	public static final Short RETIRO_VOLUNTARIO = 4;
	public static final Short OTRO = 5;
	/**
	 * 
	 */
	private static final long serialVersionUID = 4387583883246442169L;

	@Id
	@Column(name = "id", nullable = false)
	private Short id;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "internment", nullable = false)
	private Boolean internment;

	@Column(name = "emergency_care", nullable = false)
	private Boolean emergencyCare;

}
