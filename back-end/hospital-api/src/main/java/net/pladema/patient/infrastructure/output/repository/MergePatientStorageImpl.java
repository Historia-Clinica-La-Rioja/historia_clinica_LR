package net.pladema.patient.infrastructure.output.repository;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.audit.service.domain.enums.EActionType;
import net.pladema.patient.application.mergepatient.exceptions.MergePatientException;
import net.pladema.patient.application.mergepatient.exceptions.MergePatientExceptionEnum;
import net.pladema.patient.application.port.MergePatientStorage;
import net.pladema.patient.repository.MergedInactivePatientRepository;
import net.pladema.patient.repository.MergedPatientRepository;
import net.pladema.patient.repository.PatientRepository;
import net.pladema.patient.repository.entity.MergedInactivePatient;
import net.pladema.patient.repository.entity.MergedPatient;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.service.PatientService;
import net.pladema.person.controller.dto.BasicPersonalDataDto;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.service.PersonService;

@Service
@Slf4j
@RequiredArgsConstructor
public class MergePatientStorageImpl implements MergePatientStorage {

	private final PatientRepository patientRepository;

	private final PatientService patientService;

	private final PersonService personService;
	private final MergedPatientRepository mergedPatientRepository;

	private final MergedInactivePatientRepository mergedInactivePatientRepository;

	@Override
	public Boolean existPatientById(Integer id) {
		log.debug("Input parameters -> patientId {}", id);
		Boolean result = patientRepository.existsById(id);
		log.debug("Output result -> {}", result);
		return result;
	}

	@Override
	public void inactivatePatient(Integer patientIdToInactivate, Integer referencePatientId, Integer institutionId) {
		log.debug("Input parameters -> patientIdToInactivate {}, referencePatientId {}, institutionId {} ", patientIdToInactivate, referencePatientId, institutionId);
		patientRepository.findById(patientIdToInactivate).orElseThrow(() -> new MergePatientException(MergePatientExceptionEnum.PATIENT_NOT_EXISTS, String.format("El paciente a inactivar con id %s no se encuentra", patientIdToInactivate)));
		patientRepository.deleteById(patientIdToInactivate);
		auditActionPatient(institutionId, patientIdToInactivate, EActionType.DELETE);
	}

	@Override
	public void updatePersonByPatientId(Integer patientId, BasicPersonalDataDto basicPersonData, Integer institutionId) {
		log.debug("Input parameters -> patientId {}, basicPersonData {}", patientId, basicPersonData);
		if (!existPatientById(patientId))
			throw new MergePatientException(MergePatientExceptionEnum.PATIENT_NOT_EXISTS, String.format("El paciente con id %s no existe", patientId));
		Person person = personService.findByPatientId(patientId).orElseThrow(() -> new MergePatientException(MergePatientExceptionEnum.ASSOCIATED_PERSON_NOT_FOUND, String.format("No se encuentra persona asociada al paciente con id %s", patientId)));
		person.setFirstName(basicPersonData.getFirstName());
		person.setMiddleNames(basicPersonData.getMiddleNames());
		person.setLastName(basicPersonData.getLastName());
		person.setOtherLastNames(basicPersonData.getOtherLastNames());
		person.setIdentificationTypeId(basicPersonData.getIdentificationTypeId());
		person.setIdentificationNumber(basicPersonData.getIdentificationNumber());
		person.setBirthDate(basicPersonData.getBirthDate());
		personService.addPerson(person);
		auditActionPatient(institutionId, patientId, EActionType.UPDATE);
	}


	private void auditActionPatient(Integer institutionId, Integer patientId, EActionType eActionType) {
		log.debug("Input parameters -> institutionId {}, patientId {}, eActionType {}", institutionId, patientId, eActionType);
		patientService.auditActionPatient(institutionId, patientId, eActionType);
	}

	@Override
	public void saveMergeHistoricData(Integer activePatientId, List<Integer> inactivePatientIds) {
		log.debug("Input parameters -> activePatientId {}, list inactivePatientIds {}", activePatientId, inactivePatientIds);
		MergedPatient mergedPatient = mergedPatientRepository.save(new MergedPatient(activePatientId));
		inactivePatientIds.forEach(inactivePatientId -> mergedInactivePatientRepository.save(MergedInactivePatient.builder().mergedPatientId(mergedPatient.getId()).inactivePatientId(inactivePatientId).build()));
	}

}
