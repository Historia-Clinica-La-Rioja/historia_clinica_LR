package ar.lamansys.refcounterref.application.getreferencesummary;

import ar.lamansys.refcounterref.application.getreference.exceptions.ReferenceException;
import ar.lamansys.refcounterref.application.getreference.exceptions.ReferenceExceptionEnum;
import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetReferenceSummary {

	private final ReferenceStorage referenceStorage;

	public List<ReferenceSummaryBo> run(Integer patientId, Integer clinicalSpecialtyId, Integer diaryId) {
		assertContextValid(patientId, clinicalSpecialtyId, diaryId);
		log.debug("Input parameters -> patientId {}, clinicalSpecialtyId {}, diaryId {}", patientId, clinicalSpecialtyId, diaryId);
		List<ReferenceSummaryBo> result = referenceStorage.getReferencesSummary(patientId, clinicalSpecialtyId, diaryId);
		return result;
	}

	private void assertContextValid(Integer patientId, Integer clinicalSpecialtyId, Integer diaryId) {
		if (patientId == null)
			throw new ReferenceException(ReferenceExceptionEnum.NULL_PATIENT_ID, "El id del paciente es obligatorio");
		if (clinicalSpecialtyId == null)
			throw new ReferenceException(ReferenceExceptionEnum.NULL_CLINICAL_SPECIALTY_ID, "El id de especialidad es obligatorio");
		if (diaryId == null)
			throw new ReferenceException(ReferenceExceptionEnum.NULL_DIARY_ID, "El id de la agenda es obligatorio");
	}

}
