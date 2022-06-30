package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.shared.infrastructure.input.service.EIndicationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class NursingRecordBo {

	private Integer id;

	private IndicationBo indication;

	private LocalDateTime scheduledAdministrationTime;

	private String event;

	private Short statusId;

	private String observation;

	private LocalDateTime administrationTime;


}
