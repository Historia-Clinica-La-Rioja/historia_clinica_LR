package ar.lamansys.refcounterref.infraestructure.output.repository.patient;

import ar.lamansys.refcounterref.application.port.ReferencePatientStorage;
import ar.lamansys.refcounterref.domain.reference.ReferencePatientBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReferencePatientStorageImpl implements ReferencePatientStorage {

	private final SharedPatientPort sharedPatientPort;

	private final SharedPersonPort sharedPersonPort;

	@Override
	public ReferencePatientBo getPatientInfo(Integer patientId) {
		var patientData = sharedPatientPort.getBasicDataFromPatient(patientId);
		return mapToBo(patientData, patientId);
	}

	public ReferencePatientBo mapToBo(BasicPatientDto patientData, Integer patientId) {
		String patientFullName = sharedPersonPort.parseCompletePersonName(patientData.getFirstName(), patientData.getMiddleName(), patientData.getLastName(),
				patientData.getPerson().getOtherLastNames(), patientData.getPerson().getNameSelfDetermination());
		return new ReferencePatientBo(patientId, patientFullName, patientData.getIdentificationNumber(), patientData.getIdentificationType(), patientData.getPerson().getEmail());
	}

}
