package net.pladema.establishment.service;

import net.pladema.establishment.service.domain.CareLineBo;

import java.util.List;

public interface CareLineService {

    List<CareLineBo> getCareLines();

	List<CareLineBo> getCareLinesByClinicalSpecialtyAndInstitutionId(Integer institutionId, Integer clinicalSpecialtyId);
	
	List<CareLineBo> getCareLinesByProblemsSctidsAndDestinationInstitutionIdWithActiveDiaries(List<String> problemSnomedIds, Integer destinationInstitutionId);

	List<CareLineBo> getCareLinesAttachedToInstitution();

}