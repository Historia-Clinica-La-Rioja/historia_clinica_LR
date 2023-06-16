package net.pladema.clinichistory.requests.servicerequests.service.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.requests.controller.dto.TranscribedPrescriptionDto;

import javax.annotation.Nullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TranscribedServiceRequestBo {

	private Integer id;

	private Integer patientId;

	private SnomedBo healthCondition;

	private SnomedBo study;

	private String healthcareProfessionalName;

	@Nullable
	private String institutionName;

	public TranscribedServiceRequestBo (TranscribedPrescriptionDto transcribedPrescriptionDto, Integer patientId){
		this.patientId = patientId;
		this.healthCondition =  new SnomedBo(transcribedPrescriptionDto.getHealthCondition().getSctid(), transcribedPrescriptionDto.getHealthCondition().getPt());
		this.study = new SnomedBo(transcribedPrescriptionDto.getStudy().getSctid(), transcribedPrescriptionDto.getStudy().getPt());
		this.healthcareProfessionalName = transcribedPrescriptionDto.getHealthcareProfessionalName();
		this.institutionName = transcribedPrescriptionDto.getInstitutionName();
	}
}
