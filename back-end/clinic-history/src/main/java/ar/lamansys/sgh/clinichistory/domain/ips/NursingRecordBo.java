package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NursingRecordBo {

	private Integer id;

	private IndicationBo indication;

	private LocalDateTime scheduledAdministrationTime;

	private String event;

	private Short statusId;

	private String observation;

	private LocalDateTime administrationTime;

	private Integer updatedBy;

	private String updatedByName;

	private String updateReason;

}
