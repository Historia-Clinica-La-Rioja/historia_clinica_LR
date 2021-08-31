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
@Table(name = "observation_status")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ObservationStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	public static final String REGISTRY =  "307827002";
	public static final String PRELIMINARY = "261420005";
	public static final String FINAL = "261782000";
	public static final String MODIFIED = "18307000";
	public static final String FIXED = "33714007";
	public static final String CANCELED = "89925002";
	public static final String ERROR = "723510000";
	public static final String UNKNOWN = "261665006";

	@Id
	@Column(name = "id", length = 20)
	private String id;

	@Column(name = "description", nullable = false, length = 100)
	private String description;

}
