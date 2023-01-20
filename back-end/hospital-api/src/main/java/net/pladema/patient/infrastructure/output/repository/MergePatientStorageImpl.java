package net.pladema.patient.infrastructure.output.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.audit.service.domain.enums.EActionType;
import net.pladema.patient.application.port.exceptions.MergePatientException;
import net.pladema.patient.application.port.exceptions.MergePatientExceptionEnum;
import net.pladema.patient.application.port.MergePatientStorage;
import net.pladema.patient.repository.MergedInactivePatientRepository;
import net.pladema.patient.repository.MergedPatientRepository;
import net.pladema.patient.repository.PatientRepository;
import net.pladema.patient.repository.entity.MergedInactivePatient;
import net.pladema.patient.repository.entity.MergedPatient;
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
	public void inactivatePatient(Integer patientIdToInactivate, Integer referencePatientId, Integer institutionId) {
		log.debug("Input parameters -> patientIdToInactivate {}, referencePatientId {}, institutionId {} ", patientIdToInactivate, referencePatientId, institutionId);
		patientRepository.findById(patientIdToInactivate).orElseThrow(() -> new MergePatientException(MergePatientExceptionEnum.PATIENT_NOT_EXISTS, String.format("El paciente a inactivar con id %s no se encuentra", patientIdToInactivate)));
		patientRepository.deleteById(patientIdToInactivate);
		auditActionPatient(institutionId, patientIdToInactivate, EActionType.DELETE);
	}

	@Override
	public void reactivatePatient(Integer patientIdToReactivate, Integer institutionId) {
		log.debug("Input parameters -> patientIdToReactivate {}, institutionId {}", patientIdToReactivate, institutionId);
		MergedInactivePatient mip = mergedInactivePatientRepository.findByInactivePatientId(patientIdToReactivate)
				.orElseThrow(() -> new MergePatientException(MergePatientExceptionEnum.PATIENT_INACTIVE_NOT_EXISTS, String.format("El paciente con id %s no se encuentra inactivo", patientIdToReactivate)));
		patientRepository.reactivate(mip.getInactivePatientId());
		auditActionPatient(institutionId, patientIdToReactivate, EActionType.UPDATE);
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

	@Override
	public void saveMergeHistoricData(Integer activePatientId, List<Integer> inactivePatientIds) {
		log.debug("Input parameters -> activePatientId {}, list inactivePatientIds {}", activePatientId, inactivePatientIds);
		MergedPatient mergedPatient = mergedPatientRepository.save(new MergedPatient(activePatientId));
		inactivePatientIds.forEach(inactivePatientId -> mergedInactivePatientRepository.save(MergedInactivePatient.builder().mergedPatientId(mergedPatient.getId()).inactivePatientId(inactivePatientId).build()));
	}

	@Override
	public List<Integer> getInactivePatientsByActivePatientId(Integer patientId) {
		log.debug("Input parameters -> patientId {}", patientId);
		List<Integer> result = mergedInactivePatientRepository.findAllInactivePatientIdByActivePatientId(patientId);
		log.debug("Output result -> {}", result);
		return result;
	}

	@Override
	public void deleteMergeHistoricData(Integer patientId, List<Integer> inactivePatientIds) {
		log.debug("Input parameters -> patientId {}, list inactivePatientIds {}", patientId, inactivePatientIds);
		List<MergedInactivePatient> mergedInactivePatients = mergedInactivePatientRepository.findAlldByInactivePatientIds(inactivePatientIds);
		mergedInactivePatientRepository.deleteAll(mergedInactivePatients);
		mergedInactivePatients.stream()
				.map(MergedInactivePatient::getMergedPatientId)
				.distinct().filter(mpId-> !hasMergeInactivePatients(mpId))
				.forEach(mergedPatientRepository::deleteById);
	}

	@Override
	public void assertBasicPersonData(BasicPersonalDataDto basicData) {
		if (basicData.getFirstName() == null)
			throw new MergePatientException(MergePatientExceptionEnum.NULL_FIRST_NAME, "El nombre de la persona es obligatorio");
		if (basicData.getLastName() == null)
			throw new MergePatientException(MergePatientExceptionEnum.NULL_LAST_NAME, "El apellido de la persona es obligatorio");
		if (basicData.getIdentificationTypeId() == null)
			throw new MergePatientException(MergePatientExceptionEnum.NULL_IDENTIFICATION_TYPE, "El tipo de documento de la persona es obligatorio");
		if (basicData.getIdentificationNumber() == null)
			throw new MergePatientException(MergePatientExceptionEnum.NULL_IDENTIFICATION_NUMBER, "El numero de documento de la persona es obligatorio");
		if (basicData.getBirthDate() == null)
			throw new MergePatientException(MergePatientExceptionEnum.NULL_BIRTH_DATE, "La fecha de nacimiento de la persona es obligatoria");

	}

	private Boolean existPatientById(Integer id) {
		log.debug("Input parameters -> patientId {}", id);
		Boolean result = patientRepository.existsById(id);
		log.debug("Output result -> {}", result);
		return result;
	}

	private void auditActionPatient(Integer institutionId, Integer patientId, EActionType eActionType) {
		log.debug("Input parameters -> institutionId {}, patientId {}, eActionType {}", institutionId, patientId, eActionType);
		patientService.auditActionPatient(institutionId, patientId, eActionType);
	}

	private Boolean hasMergeInactivePatients(Integer mergePatientId) {
		log.debug("Input parameters -> mergePatientId {}", mergePatientId);
		Boolean result = mergedInactivePatientRepository.existsByMergePatientId(mergePatientId);
		log.debug("Output result {}", result);
		return result;
	}

}
