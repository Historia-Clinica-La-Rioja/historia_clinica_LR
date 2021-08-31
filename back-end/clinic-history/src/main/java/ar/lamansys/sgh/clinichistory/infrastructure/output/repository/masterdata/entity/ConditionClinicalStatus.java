package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "condition_clinical_status")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ConditionClinicalStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	public static final String ACTIVE =  "55561003";
	public static final String INACTIVE = "73425007";
	public static final String REMISSION = "277022003";
	public static final String SOLVED = "723506003";

	@Id
	@Column(name = "id", length = 20)
	private String id;

	@Column(name = "description", nullable = false, length = 100)
	private String description;

	public static String[] downState(){
		return new String[]{REMISSION, SOLVED};
	}
}
