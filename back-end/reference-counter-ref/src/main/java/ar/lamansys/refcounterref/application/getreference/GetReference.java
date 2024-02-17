package ar.lamansys.refcounterref.application.getreference;

import ar.lamansys.refcounterref.application.getreference.exceptions.ReferenceException;
import ar.lamansys.refcounterref.application.getreference.exceptions.ReferenceExceptionEnum;
import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import ar.lamansys.refcounterref.domain.reference.ReferenceDataBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedLoggedUserPort;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetReference {

    private final ReferenceStorage referenceStorage;

	private final SharedLoggedUserPort sharedLoggedUserPort;

    public List<ReferenceDataBo> run(Integer institutionId, Integer patientId, List<Integer> clinicalSpecialtyIds) {
        log.debug("Input parameters -> institutionId{}, patientId {}, clinicalSpecialtyIds {} ", institutionId, patientId, clinicalSpecialtyIds);
        assertContextValid(patientId, clinicalSpecialtyIds);
		Integer loggedUserId = UserInfo.getCurrentAuditor();
		List<Short> loggedUserRoleIds = sharedLoggedUserPort.getLoggedUserRoleIds(institutionId, loggedUserId);
        List<ReferenceDataBo> result = referenceStorage.getReferences(patientId, clinicalSpecialtyIds, loggedUserRoleIds);
		log.debug("Output -> {}", result);
        return result;
    }

    private void assertContextValid(Integer patientId, List<Integer> clinicalSpecialtyIds) {
        if (patientId == null)
            throw new ReferenceException(ReferenceExceptionEnum.NULL_PATIENT_ID, "El id del paciente es obligatorio");
        if (clinicalSpecialtyIds.isEmpty())
            throw new ReferenceException(ReferenceExceptionEnum.NULL_CLINICAL_SPECIALTY_ID, "Es obligatorio al menos un id de especialidad");
    }

}
