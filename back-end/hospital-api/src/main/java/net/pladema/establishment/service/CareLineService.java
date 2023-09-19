package net.pladema.establishment.service;

import net.pladema.establishment.service.domain.CareLineBo;

import java.util.List;

public interface CareLineService {

    List<CareLineBo> getCareLines();

	List<CareLineBo> getCareLinesByClinicalSpecialtyAndInstitutionId(Integer institutionId, Integer clinicalSpecialtyId);
	
	List<CareLineBo> getAllByProblemsAndProvinceId(List<String> snomedSctids, Integer institutionId);

	List<CareLineBo> getCareLinesAttachedToInstitutions();

	List<CareLineBo> getByInstitutionIdAndPracticesId(Integer institutionId, List<Integer> practicesId);

	List<CareLineBo> getVirtualConsultationCareLinesByInstitutionId(Integer institutionId);

	List<CareLineBo> getCareLinesAttachedToInstitution(Integer institutionId);

}