package ar.lamansys.refcounterref.application.getreferencesummary;

import ar.lamansys.refcounterref.application.getreference.exceptions.ReferenceException;
import ar.lamansys.refcounterref.application.getreference.exceptions.ReferenceExceptionEnum;
import ar.lamansys.refcounterref.application.port.HistoricReferenceRegulationStorage;
import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo;
import ar.lamansys.refcounterref.domain.referenceregulation.ReferenceRegulationBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedLoggedUserPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.institutionalgroup.SharedInstitutionalGroupPort;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetReferenceSummary {

	private final ReferenceStorage referenceStorage;
	private final HistoricReferenceRegulationStorage historicReferenceRegulationStorage;
	private final SharedLoggedUserPort sharedLoggedUserPort;
	private final SharedInstitutionalGroupPort sharedInstitutionalGroupPort;

	public List<ReferenceSummaryBo> run(Integer institutionId, Integer patientId, Integer clinicalSpecialtyId, Integer careLineId, Integer practiceId) {
		assertContextValid(patientId, clinicalSpecialtyId, practiceId);
		log.debug("Input parameters -> patientId {}, clinicalSpecialtyId {}, careLineId {}, practiceId {}", patientId, clinicalSpecialtyId, careLineId, practiceId);
		Integer userId = UserInfo.getCurrentAuditor();
		List<Short> userRoleIds = sharedLoggedUserPort.getLoggedUserRoleIds(-1, userId);
		userRoleIds.addAll(sharedLoggedUserPort.getLoggedUserRoleIds(institutionId, userId));
		List<ReferenceSummaryBo> result = referenceStorage.getReferencesSummary(patientId, clinicalSpecialtyId, careLineId, practiceId, userRoleIds);
		addRegulationStateToReferences(result);
		result = filterByPermissions(result, userId);
		log.debug("OUTPUT -> {}", result);
		return result;
	}

	private void assertContextValid(Integer patientId, Integer clinicalSpecialtyId, Integer practiceId) {
		if (patientId == null)
			throw new ReferenceException(ReferenceExceptionEnum.NULL_PATIENT_ID, "El id del paciente es obligatorio");
		if (clinicalSpecialtyId == null && practiceId == null)
			throw new ReferenceException(ReferenceExceptionEnum.NULL_TYPE_ATTENTION_ID, "Debe seleccionar al menos un tipo de atencion para realizar la busqueda");
	}

	private List<ReferenceSummaryBo> filterByPermissions(List<ReferenceSummaryBo> referenceList, Integer userId){
		if (sharedLoggedUserPort.hasLocalManagerRoleOrRegionalManagerRole(userId)) {
			List<Integer> institutionIds = sharedInstitutionalGroupPort.getInstitutionIdsByUserId(userId);
			referenceList = referenceList.stream().filter(r -> institutionIds.contains(r.getDestinationInstitutionId())).collect(Collectors.toList());
		}
		return referenceList;
	}

	private void addRegulationStateToReferences(List<ReferenceSummaryBo> references){
		references.forEach(r -> {
			Optional<ReferenceRegulationBo> referenceRegulation = historicReferenceRegulationStorage.getByReferenceId(r.getId());
			referenceRegulation.ifPresent(regulation -> r.setRegulationState(regulation.getState()));
		});
	}

}
