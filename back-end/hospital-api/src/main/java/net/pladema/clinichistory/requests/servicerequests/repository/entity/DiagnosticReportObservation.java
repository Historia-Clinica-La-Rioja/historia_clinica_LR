package net.pladema.clinichistory.requests.servicerequests.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Optional;

@Entity
@Table(name = "diagnostic_report_observation")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Where(clause = "deleted=false")
public class DiagnosticReportObservation extends SGXAuditableEntity<Integer> {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "diagnostic_report_observation_group_id", nullable = false)
	private Integer diagnosticReportObservationGroupId;

	@Column(name = "procedure_parameter_id", nullable = false)
	private Integer procedureParameterId;

	@Column(name = "value", nullable = false)
	private String value;

	@Column(name = "unit_of_measure_id", nullable = true)
	private Short unitOfMeasureId;

	@Column(name = "value_numeric", nullable = true)
	private BigDecimal valueNumeric;

	public static DiagnosticReportObservation newNumericObservation(
		Integer diagnosticReportGroupId,
		Integer procedureParameterId,
		Optional<String> value,
		Short unitOfMeasureId, Optional<BigDecimal> valueNumeric) {
		return new DiagnosticReportObservation(
			null,
			diagnosticReportGroupId,
			procedureParameterId,
			value.orElse(""),
			unitOfMeasureId,
			valueNumeric.orElse(null)
		);
	}

	public static DiagnosticReportObservation newNonNumericObservation(Integer diagnosticReportGroupId, Integer procedureParameterId, Optional<String> value) {
		return new DiagnosticReportObservation(
			null,
			diagnosticReportGroupId,
			procedureParameterId,
			value.orElse(""),
			null,
			null
		);
	}

	public void setValue(Optional<String> value) {
		this.value = value.orElse("");
	}

	public void setValue(String value) {
		this.setValue(Optional.ofNullable(value));
	}
}
