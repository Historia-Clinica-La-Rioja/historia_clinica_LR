package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "diet")
@Getter
@Setter
@NoArgsConstructor
public class Diet extends Indication {

	/*
	 */
	private static final long serialVersionUID = 2873716268832417941L;

	@Column(name = "description", nullable = false, columnDefinition = "TEXT")
	private String description;

}
