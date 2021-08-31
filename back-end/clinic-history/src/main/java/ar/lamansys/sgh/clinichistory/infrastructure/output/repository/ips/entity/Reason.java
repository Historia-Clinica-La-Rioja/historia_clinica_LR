package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "reasons")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Reason implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Id
	@Column(name = "id", length = 20)
	private String id;

	@Column(name = "description", length = 255, nullable = false)
	private String description;

}
