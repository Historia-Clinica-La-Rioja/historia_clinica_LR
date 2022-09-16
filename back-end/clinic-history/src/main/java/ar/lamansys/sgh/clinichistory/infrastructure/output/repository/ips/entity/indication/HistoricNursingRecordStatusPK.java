package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class HistoricNursingRecordStatusPK implements Serializable {


	@Column(name = "nursing_record_id", nullable = false)
	private Integer nursingRecordId;

	@Column(name = "changed_status_date", nullable = false)
	private LocalDateTime changedStatusDate;

}
