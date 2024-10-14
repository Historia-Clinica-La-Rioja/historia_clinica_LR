package net.pladema.emergencycare.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.emergencycare.triage.domain.EmergencyCareTriageBo;
import net.pladema.medicalconsultation.diary.service.domain.ProfessionalPersonBo;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class EmergencyCareAttentionPlaceDetailBo {

	private EmergencyCarePatientBo patient;
	private String reason;
	private Short emergencyCareTypeId;
	private Short emergencyCareStateId;
	private ProfessionalPersonBo professional;
	private LocalDateTime updatedOn;
	private EmergencyCareTriageBo lastTriage;
}
