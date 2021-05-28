package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "medication_statement_status")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MedicationStatementStatus implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	public static final String ERROR = "723510000";
	public static final String ACTIVE = "55561003";
	public static final String SUSPENDED = "385655000";
	public static final String STOPPED = "6155003";
	public static final List<String> STATES = List.of(ACTIVE, SUSPENDED, STOPPED, ERROR);


	@Id
	@Column(name = "id", length = 20)
	private String id;

	@Column(name = "description", nullable = false, length = 100)
	private String description;

}
