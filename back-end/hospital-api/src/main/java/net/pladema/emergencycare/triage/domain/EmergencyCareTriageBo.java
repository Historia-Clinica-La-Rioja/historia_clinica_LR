package net.pladema.emergencycare.triage.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.emergencycare.triage.service.domain.TriageCategoryBo;
import net.pladema.establishment.domain.ClinicalSpecialtySectorBo;
import net.pladema.medicalconsultation.diary.service.domain.ProfessionalPersonBo;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EmergencyCareTriageBo {

	private Integer id;
	private String name;
	private String color;
	private List<ReasonBo> reasons;
	private ProfessionalPersonBo creator;
	private ClinicalSpecialtySectorBo clinicalSpecialtySectorBo;
	private LocalDateTime createdOn;

	public EmergencyCareTriageBo(TriageBo triageBo, TriageCategoryBo triageCategoryBo){
		this.reasons = triageBo.getReasons();
		this.creator = triageBo.getCreator();
		this.clinicalSpecialtySectorBo = triageBo.getClinicalSpecialtySectorBo();
		this.createdOn = triageBo.getCreatedOn();
		this.id = Integer.valueOf(triageCategoryBo.getId());
		this.name = triageCategoryBo.getName();
		this.color = triageCategoryBo.getColorCode();
	}
}
