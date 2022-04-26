package net.pladema.medicalconsultation.appointment.controller.mapper;

import ar.lamansys.sgh.shared.infrastructure.input.service.EMedicalCoverageTypeDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalCoverageDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalPatientCoverageDto;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;

public class ExternalPatientCoverageMapper {

	public static ExternalPatientCoverageDto mapToExternalPatientCoverageDto(PatientMedicalCoverageBo bo){
		if(bo.getId() != null) {
			var medicalCoverageDetails = bo.getMedicalCoverage();
			var type = EMedicalCoverageTypeDto.map(bo.getMedicalCoverage().getType()).getValue();
			var medicalCoverage = new ExternalCoverageDto();
			medicalCoverage.setId(medicalCoverageDetails.getId());
			medicalCoverage.setCuit(medicalCoverageDetails.getCuit());
			medicalCoverage.setPlan(bo.getPlanName());
			medicalCoverage.setName(medicalCoverageDetails.getName());
			medicalCoverage.setType(type);
			var conditionId = bo.getCondition() == null ? null : bo.getCondition().getId();
			return new ExternalPatientCoverageDto(medicalCoverage, bo.getAffiliateNumber(), bo.getActive(), bo.getVigencyDate(), conditionId);
		}
		return new ExternalPatientCoverageDto();
	}

}