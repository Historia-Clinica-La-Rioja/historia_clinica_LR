package net.pladema.patient.service;

import net.pladema.audit.service.domain.enums.EActionType;
import net.pladema.patient.controller.dto.AuditablePatientInfoDto;
import net.pladema.patient.controller.dto.MergedPatientSearchFilter;
import net.pladema.patient.controller.dto.PatientRegistrationSearchFilter;
import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.repository.domain.PatientPersonVo;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.repository.entity.PatientType;
import net.pladema.patient.service.domain.MergedPatientSearch;
import net.pladema.patient.service.domain.PatientGenderAgeBo;
import net.pladema.patient.service.domain.PatientRegistrationSearch;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.person.repository.domain.PersonSearchResultVo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PatientService {

	List<PatientSearch> searchPatient(PatientSearchFilter searchFilter);

    Page<PatientSearch> searchPatientOptionalFilters(PatientSearchFilter searchFilter, Pageable pageable);

    Optional<Patient> getActivePatient(Integer patientId);

	Optional<Patient> getPatient(Integer patientId);

	List<Patient> getPatients(Set<Integer> patients);
    
    Patient addPatient(Patient patientToSave);

    List<PatientPersonVo> getAllValidatedPatients();

    void updatePatientPermanent(PatientPersonVo patientPersonVo, Integer nationalId);

    void auditActionPatient(Integer institutionId, Integer patientId, EActionType eActionType);

	Optional<String> getIdentificationNumber(Integer patientId);

	void persistSelectionForAnAudict(Integer patientId, Integer institutionId, String message);

	AuditablePatientInfoDto getAuditablePatientInfo(Integer patientId);

	List<PatientRegistrationSearch> getPatientsRegistrationByFilter(PatientRegistrationSearchFilter searchFilter);

	List<PatientRegistrationSearch> getPatientRegistrationById(Integer patientId);

	List<PatientType> getPatientTypesForAuditor();
	
	List<MergedPatientSearch> getMergedPatientsByFilter(MergedPatientSearchFilter searchFilter);

	List<PersonSearchResultVo> getMergedPersonsByPatientId(Integer activePatientId);

	List<PatientRegistrationSearch> getPatientsToAudit();

	void assertHasActiveEncountersByPatientId(Integer patientId);

	List<Patient> getLongTermTemporaryPatientIds(LocalDateTime maxDate, Short limit);

	Optional<PatientGenderAgeBo> getPatientGenderAge(Integer patientId);

}
