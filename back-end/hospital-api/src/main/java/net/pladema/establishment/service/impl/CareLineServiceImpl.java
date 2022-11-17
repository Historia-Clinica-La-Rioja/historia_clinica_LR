package net.pladema.establishment.service.impl;

import lombok.RequiredArgsConstructor;
import net.pladema.establishment.repository.CareLineInstitutionSpecialtyRepository;
import net.pladema.establishment.repository.CareLineRepository;
import net.pladema.establishment.service.CareLineService;
import net.pladema.establishment.service.domain.CareLineBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CareLineServiceImpl implements CareLineService {

    private static final Logger LOG = LoggerFactory.getLogger(CareLineServiceImpl.class);

    private static final String OUTPUT = "Output -> {}";

    private final CareLineRepository careLineRepository;
	private final CareLineInstitutionSpecialtyRepository careLineInstitutionSpecialtyRepository;

    @Override
    public List<CareLineBo> getCareLines() {
        LOG.debug("No input parameters");
        List<CareLineBo> careLines = careLineRepository.getCareLinesWhitClinicalSpecialties()
                .stream()
                .map(careLine -> new CareLineBo(careLine.getId(), careLine.getDescription()))
                .collect(Collectors.toList());
        LOG.trace(OUTPUT, careLines);
        return careLines;
    }

	@Override
	public List<CareLineBo> getCareLinesByClinicalSpecialtyAndInstitutionId(Integer institutionId, Integer clinicalSpecialtyId) {
		LOG.debug("Input parameter ->, institutionId {}, clinicalSpecialtyId {}", institutionId, clinicalSpecialtyId);
		List<CareLineBo> result = careLineRepository.getCareLinesByClinicalSpecialtyAndInstitutionId(institutionId, clinicalSpecialtyId);
		LOG.trace(OUTPUT, result);
		return result;
    }

    @Override
	public List<CareLineBo> getCareLinesByProblemsSctidsAndDestinationInstitutionIdWithActiveDiaries(List<String> problemSnomedIds, Integer destinationInstitutionId) {
		LOG.debug("Input parameters -> problemSnomedIds {}", problemSnomedIds);
		List<CareLineBo> careLines = careLineRepository.getCareLinesByProblemsSctidsAndDestinationInstitutionIdWithActiveDiaries(problemSnomedIds, destinationInstitutionId);
		LOG.trace(OUTPUT, careLines);
		return careLines;
	}

	@Override
	public List<CareLineBo> getCareLinesAttachedToInstitution() {
		List<CareLineBo> result = careLineRepository.getCareLinesAttachedToInstitution();
		result.stream().forEach(careLine -> careLine.setClinicalSpecialties(careLineInstitutionSpecialtyRepository.getClinicalSpecialtiesByCareLineId(careLine.getId())));
		LOG.debug(OUTPUT, result);
		return result;
	}

}
