package net.pladema.clinichistory.requests.medicationrequests.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "medication_request_category")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MedicationRequestCategory {

	public static final String INPATIENT = "416800000";
	public static final String OUTPATIENT = "373864002";
	public static final String COMMUNITY  = "129274004";
	public static final String DISCHARGE = "303119007";

	@Id
	@Column(name = "id", length = 20)
	private String id;

	@Column(name = "description", nullable = false, length = 50)
	private String description;

}
