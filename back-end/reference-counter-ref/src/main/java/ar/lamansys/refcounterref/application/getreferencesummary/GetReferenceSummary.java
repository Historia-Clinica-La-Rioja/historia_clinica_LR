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

	public List<ReferenceSummaryBo> run(Integer patientId, Integer clinicalSpecialtyId, Integer careLineId, Integer practiceId) {
		assertContextValid(patientId, clinicalSpecialtyId, careLineId, practiceId);
		log.debug("Input parameters -> patientId {}, clinicalSpecialtyId {}, careLineId {}, practiceId {}", patientId, clinicalSpecialtyId, careLineId, practiceId);
		List<ReferenceSummaryBo> result = referenceStorage.getReferencesSummary(patientId, clinicalSpecialtyId, careLineId, practiceId);
		return result;
	}

	private void assertContextValid(Integer patientId, Integer clinicalSpecialtyId, Integer careLineId, Integer practiceId) {
		if (patientId == null)
			throw new ReferenceException(ReferenceExceptionEnum.NULL_PATIENT_ID, "El id del paciente es obligatorio");
		if (clinicalSpecialtyId == null && practiceId == null)
			throw new ReferenceException(ReferenceExceptionEnum.NULL_TYPE_ATTENTION_ID, "Debe seleccionar al menos un tipo de atencion para realizar la busqueda");
		if (careLineId == null)
			throw new ReferenceException(ReferenceExceptionEnum.NULL_CARE_LINE_ID, "El id de la línea de cuidado es obligatorio");
	}

}
