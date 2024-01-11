package net.pladema.establishment.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedLoggedUserPort;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.application.port.CareLineInstitutionPracticeStorage;
import net.pladema.establishment.application.port.carelineproblem.CareLineProblemStorage;
import net.pladema.establishment.repository.CareLineInstitutionSpecialtyRepository;
import net.pladema.establishment.repository.CareLineRepository;
import net.pladema.establishment.service.CareLineService;
import net.pladema.establishment.service.ClinicalSpecialtyCareLineService;
import net.pladema.establishment.service.domain.CareLineBo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CareLineServiceImpl implements CareLineService {

    private static final Logger LOG = LoggerFactory.getLogger(CareLineServiceImpl.class);

    private static final String OUTPUT = "Output -> {}";

    private final CareLineRepository careLineRepository;

	private final CareLineInstitutionSpecialtyRepository careLineInstitutionSpecialtyRepository;

	private final CareLineProblemStorage careLineProblemStorage;

	private final CareLineInstitutionPracticeStorage careLineInstitutionPracticeStorage;

	private final SharedLoggedUserPort sharedLoggedUserPort;

	private final ClinicalSpecialtyCareLineService clinicalSpecialtyCareLineService;

    @Override
    public List<CareLineBo> getCareLines() {
        LOG.debug("No input parameters");
		Integer loggedUserId = UserInfo.getCurrentAuditor();
		List<Short> loggedUserRoleIds = sharedLoggedUserPort.getLoggedUserRoleIds(-1, loggedUserId);
		List<CareLineBo> careLines = careLineRepository.getCareLinesWhitClinicalSpecialties(loggedUserRoleIds)
                .stream()
                .map(careLine -> new CareLineBo(careLine.getId(), careLine.getDescription()))
				.collect(Collectors.toList());
		careLines.forEach(careLine -> careLine.setClinicalSpecialties(clinicalSpecialtyCareLineService.getClinicalSpecialties(careLine.getId())));
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
	public List<CareLineBo> getAllByProblems(List<String> snomedSctids, Integer institutionId, Integer loggedUserId) {
		LOG.debug("Input parameters -> snomedSctids {}, institutionId {}, loggedUserId {}", snomedSctids, institutionId, loggedUserId);
		List<Short> loggedUserRoleIds = sharedLoggedUserPort.getLoggedUserRoleIds(institutionId, loggedUserId);
		List<CareLineBo> careLines = careLineRepository.getCareLinesAttachedToInstitutions(loggedUserRoleIds);
		List<CareLineBo> result = this.getCareLinesWithAllProblems(careLines, snomedSctids);
		result.forEach(careLine -> careLine.setClinicalSpecialties(careLineInstitutionSpecialtyRepository.getClinicalSpecialtiesByCareLineId(careLine.getId())));
		LOG.trace(OUTPUT, result);
		return result;
	}

	@Override
	public List<CareLineBo> getCareLinesAttachedToInstitutions(Integer institutionId, Integer loggedUserId) {
		log.debug("Input parameters -> institutionId {}, loggedUserId {}", institutionId, loggedUserId);
		List<Short> loggedUserRoleIds = sharedLoggedUserPort.getLoggedUserRoleIds(institutionId, loggedUserId);
		List<CareLineBo> result = careLineRepository.getCareLinesAttachedToInstitutions(loggedUserRoleIds);
		result.forEach(careLine -> careLine.setClinicalSpecialties(careLineInstitutionSpecialtyRepository.getClinicalSpecialtiesByCareLineId(careLine.getId())));
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<CareLineBo> getByInstitutionIdAndPracticesId(Integer institutionId, List<Integer> practicesId) {
		log.debug("Input parameters -> practicesId {}, institutionId {}", practicesId, institutionId);
		List<CareLineBo> careLinesByInstitution = careLineRepository.getAllByInstitutionId(institutionId);
		List<CareLineBo> result = this.getCareLinesWithAllPractices(careLinesByInstitution, practicesId);
		log.trace(OUTPUT, result);
		return result;
	}

	@Override
	public List<CareLineBo> getVirtualConsultationCareLinesByInstitutionId(Integer institutionId) {
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<CareLineBo> result = careLineRepository.getVirtualConsultationCareLinesByInstitutionId(institutionId);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<CareLineBo> getCareLinesAttachedToInstitution(Integer institutionId) {
		LOG.debug("Input parameters -> institutionId {}", institutionId);
		List<CareLineBo> result = careLineRepository.getAllByInstitutionId(institutionId);
		result.stream().forEach(careLine -> careLine.setClinicalSpecialties(careLineInstitutionSpecialtyRepository.getClinicalSpecialtiesByCareLineId(careLine.getId())));
		LOG.debug(OUTPUT, result);
		return result;
	}

	public List<CareLineBo> getCareLinesWithAllProblems(List<CareLineBo> careLines, List<String> snomedSctids) {
		List<Integer> careLineIds = careLines.stream().map(CareLineBo::getId).collect(Collectors.toList());
		Map<Integer, List<SnomedBo>> problems = careLineProblemStorage.fetchBySnomedSctids(careLineIds, snomedSctids);
		return careLines.stream()
				.filter(cl -> problems.get(cl.getId())
						.stream()
						.map(SnomedBo::getSctid)
						.collect(Collectors.toSet())
						.containsAll(snomedSctids))
				.collect(Collectors.toList());
	}

	public List<CareLineBo> getCareLinesWithAllPractices(List<CareLineBo> careLines, List<Integer> practicesId) {
		List<Integer> careLineIds = careLines.stream().map(CareLineBo::getId).collect(Collectors.toList());
		Map<Integer, List<SnomedBo>> practices = careLineInstitutionPracticeStorage.fetchAllByCareLineIds(careLineIds);
		return careLines.stream()
				.filter(cl -> practices.get(cl.getId())
						.stream()
						.map(SnomedBo::getId)
						.collect(Collectors.toSet())
						.containsAll(practicesId))
				.collect(Collectors.toList());
	}
}
