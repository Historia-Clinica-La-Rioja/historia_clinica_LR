package net.pladema.emergencycare.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.emergencycare.triage.domain.TriageBo;
import net.pladema.emergencycare.triage.service.domain.TriageCategoryBo;
import net.pladema.medicalconsultation.diary.service.domain.ProfessionalPersonBo;

@Setter
@Getter
@NoArgsConstructor
public class EmergencyCareAttentionPlaceDetailBo {

	private EmergencyCarePatientBo patient;
	private String reason;
	private Short emergencyCareTypeId;
	private Short emergencyCareStateId;
	private ProfessionalPersonBo professional;
	private TriageBo lastTriage;
	private Short triageCategoryId;
	private String triageName;
	private String triageColorCode;

	public void setTriageCategoryInfo(TriageCategoryBo triageCategoryBo){
		this.triageCategoryId = triageCategoryBo.getId();
		this.triageName = triageCategoryBo.getName();
		this.triageColorCode = triageCategoryBo.getColorCode();
	}
}
