package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "diagnostic_report_status")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticReportStatus {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	public static final String REGISTERED = "1";
	public static final String PARTIAL = "255609007";
	public static final String FINAL = "261782000";
	public static final String CORRECTED = "33714007";
	public static final String APPENDED = "18403000";
	public static final String CANCELLED = "89925002";
	public static final String ERROR = "723510000";


	@Id
	@Column(name = "id", length = 20)
	private String id;

	@Column(name = "description", nullable = false, length = 100)
	private String description;

}
