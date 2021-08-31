package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity;

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
@Table(name = "condition_verification_status")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ConditionVerificationStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	public static final String DISCARDED = "260385009";
	public static final String PRESUMPTIVE = "76104008";
	public static final String CONFIRMED = "59156000";
	public static final String ERROR =  "723510000";

	@Id
	@Column(name = "id", length = 20)
	private String id;

	@Column(name = "description", nullable = false, length = 100)
	private String description;

	public static String[] downState(){
		return new String[]{DISCARDED, ERROR};
	}

	public static boolean isDownState(String verificationId) {
		return (verificationId.equalsIgnoreCase(ERROR) || verificationId.equalsIgnoreCase(DISCARDED));
	}
}
