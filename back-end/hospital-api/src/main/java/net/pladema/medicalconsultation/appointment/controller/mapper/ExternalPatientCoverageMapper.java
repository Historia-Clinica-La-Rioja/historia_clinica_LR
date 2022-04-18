package net.pladema.medicalconsultation.appointment.controller.mapper;

import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalCoverageDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalPatientCoverageDto;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;

public class ExternalPatientCoverageMapper {

	public static ExternalPatientCoverageDto mapToExternalPatientCoverageDto(PatientMedicalCoverageBo bo){
		if(bo.getId() != null) {
			var phid = bo.getPrivateHealthInsuranceDetails();
			var medicalCoverageDetails = bo.getMedicalCoverage();
			var type = bo.getMedicalCoverage().obtainCoverageType();
			var medicalCoverage = new ExternalCoverageDto();
			medicalCoverage.setId(medicalCoverageDetails.getId());
			medicalCoverage.setCuit(medicalCoverageDetails.getCuit());
			var planName = phid.getPlanName();
			if (planName != null) medicalCoverage.setPlan(planName);
			medicalCoverage.setName(medicalCoverageDetails.getName());
			medicalCoverage.setType(type);
			return new ExternalPatientCoverageDto(medicalCoverage, bo.getAffiliateNumber(), bo.getActive(), bo.getVigencyDate(), bo.getCondition().getId());
		}
		return new ExternalPatientCoverageDto();
	}

}