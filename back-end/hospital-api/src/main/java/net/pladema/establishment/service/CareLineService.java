package net.pladema.establishment.service;

import net.pladema.establishment.service.domain.CareLineBo;

import java.util.List;

public interface CareLineService {

    List<CareLineBo> getCareLines();

	List<CareLineBo> getCareLinesByClinicalSpecialtyAndInstitutionId(Integer institutionId, Integer clinicalSpecialtyId);
	
	List<CareLineBo> getAllByProblems(List<String> snomedSctids, Integer institutionId, Integer loggedUserId);

	List<CareLineBo> getCareLinesAttachedToInstitutions(Integer institutionId, Integer loggedUserId);

	List<CareLineBo> getByInstitutionIdAndPracticesId(Integer institutionId, List<Integer> practicesId);

	List<CareLineBo> getVirtualConsultationCareLinesByInstitutionId(Integer institutionId);

	List<CareLineBo> getCareLinesAttachedToInstitution(Integer institutionId);

}