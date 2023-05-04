package net.pladema.clinichistory.hospitalization.service.indication.nursingrecord.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.indication.service.diet.domain.GenericIndicationBo;

import java.time.LocalDateTime;

@Getter
@Setter
public class IntermentNursingRecordBo {

	private Integer id;

	private GenericIndicationBo indication;

	private LocalDateTime scheduledAdministrationTime;

	private String event;

	private Short statusId;

	private String observation;

	private LocalDateTime administrationTime;

}
