package ar.lamansys.sgh.publicapi.patient.infrastructure.input.rest.mapper;

import ar.lamansys.sgh.publicapi.patient.domain.PatientMedicalCoverageBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientMedicalCoverageDto;

public class FetchPatientMedicalCoveragesMapper {
	public static SharedPatientMedicalCoverageDto fromBo(PatientMedicalCoverageBo mc) {
		return SharedPatientMedicalCoverageDto.builder()
				.name(mc.getName())
				.type(mc.getType())
				.plan(mc.getPlan())
				.affiliateNumber(mc.getAffiliateNumber())
				.affiliationType(mc.getAffiliationType())
				.cuit(mc.getCuit())
				.startDate(mc.getStartDate())
				.endDate(mc.getEndDate())
				.build();
	}
}
