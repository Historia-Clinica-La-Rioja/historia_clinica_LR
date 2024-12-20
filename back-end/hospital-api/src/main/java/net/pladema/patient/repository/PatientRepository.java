package net.pladema.patient.repository;

import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.MedicationRequestValidationDispatcherPatientBo;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.emergencycare.domain.EmergencyCarePatientBo;
import net.pladema.patient.domain.DocumentPatientBo;
import net.pladema.patient.domain.FetchGlobalCoordinatesSanitaryResponsibilityAreaPatientAddressBo;
import net.pladema.patient.repository.domain.PatientPersonVo;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.service.domain.PatientGenderAgeBo;
import net.pladema.patient.service.domain.PatientRegistrationSearch;
import net.pladema.patient.service.domain.PatientSearch;

import net.pladema.sanitaryresponsibilityarea.domain.SanitaryRegionPatientMapCoordinatesBo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface PatientRepository extends SGXAuditableEntityJPARepository<Patient, Integer>, PatientRepositoryCustom, PatientRepositorySearch {

	@Query(value = " SELECT new net.pladema.patient.service.domain.PatientSearch(person, patient.id, patientType.active, 0, personExtended.nameSelfDetermination) " +
			" FROM Patient patient JOIN Person person ON patient.personId = person.id " +
			" JOIN PatientType patientType ON patient.typeId = patientType.id" +
			" LEFT JOIN PersonExtended personExtended ON personExtended.id = person.id" +
			" WHERE person.firstName = :name " +
			" OR person.lastName = :lastName " +
			" OR person.identificationNumber = :identificationNumber " +
			" OR person.birthDate = :birthDate ")
	Stream<PatientSearch> getAllByFilter(@Param("name") String name, @Param("lastName") String lastName,
										 @Param("identificationNumber") String identificationNumber, @Param("birthDate") LocalDate birthDate);

	@Query(value = " SELECT p.id " +
			"FROM UserPerson up " +
			"LEFT JOIN Patient p ON (p.personId = up.pk.personId) " +
			"WHERE up.pk.userId = :userId")
	Optional<Integer> getPatientIdByUser(@Param("userId") Integer userId);

	@Query(value = " SELECT up.pk.personId " +
			"FROM UserPerson up " +
			"WHERE up.pk.userId = :userId")
	Optional<Integer> getPersonIdByUser(@Param("userId") Integer userId);

	@Query(value = "SELECT new net.pladema.patient.repository.domain.PatientPersonVo(patient, person) " +
			"FROM Patient patient " +
			"JOIN Person person ON patient.personId = person.id " +
			"WHERE patient.typeId = :patientTypeId " +
			"AND patient.deleteable.deleted = false ")
	List<PatientPersonVo> getAllByPatientType(@Param("patientTypeId") Short patientTypeId);

	@Query(value = " SELECT p.identificationNumber " +
			"FROM Patient pa " +
			"JOIN Person p ON p.id = pa.personId " +
			"WHERE pa.id = :patientId")
	Optional<String> getIdentificationNumber(@Param("patientId") Integer patientId);

	@Query(value = " SELECT new net.pladema.patient.service.domain.PatientRegistrationSearch(pe, p.id, p.typeId, p.auditTypeId, pee.nameSelfDetermination) " +
			"FROM Patient p " +
			"JOIN Person pe ON p.personId = pe.id " +
			"LEFT JOIN PersonExtended pee ON pee.id = pe.id " +
			"WHERE p.id = :patientId AND p.typeId IN :typeIds")
	Optional<PatientRegistrationSearch> getPatientRegistrationSearchById(@Param("patientId") Integer patientId, @Param("typeIds") List<Short> typeIds);

	@Query(value = " SELECT pa " +
			"FROM Patient pa " +
			"WHERE pa.id = :patientId " +
			"AND pa.typeId != 6")
	Optional<Patient> findActivePatientById(@Param("patientId") Integer patientId);

	@Transactional(readOnly = true)
	@Query(" SELECT NEW net.pladema.patient.domain.DocumentPatientBo(pe.firstName, pe.middleNames, pe.lastName, pe.otherLastNames, pex.nameSelfDetermination, " +
			"pex.email, spg.description, pex.phonePrefix, pex.phoneNumber, a.street, a.number, a.floor, a.apartment, c.description, pr.description) " +
			"FROM Patient p " +
			"JOIN Person pe ON (pe.id = p.personId) " +
			"JOIN PersonExtended pex ON (pex.id = pe.id) " +
			"LEFT JOIN SelfPerceivedGender spg ON (spg.id = pex.genderSelfDeterminationId) " +
			"LEFT JOIN Address a ON (a.id = pex.addressId) " +
			"LEFT JOIN City c ON (c.id = a.cityId) " +
			"LEFT JOIN Province pr ON (p.id = a.provinceId) " +
			"WHERE p.id = :patientId")
    DocumentPatientBo getDocumentPatientData(@Param("patientId") Integer patientId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.patient.service.domain.PatientGenderAgeBo(p.id, pe.genderId, pe.birthDate) " +
			"FROM Patient p " +
			"JOIN Person pe ON (p.personId = pe.id) " +
			"WHERE p.id = :patientId")
	Optional<PatientGenderAgeBo> getPatientGenderAge(@Param("patientId")Integer patientId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.patient.domain.FetchGlobalCoordinatesSanitaryResponsibilityAreaPatientAddressBo(p.id, a.street, " +
			"a.number, c.description, p2.description, c2.description, a.postcode) " +
			"FROM Patient p " +
			"JOIN PersonExtended pe ON (pe.id = p.personId) " +
			"JOIN Address a ON (a.id = pe.addressId) " +
			"JOIN City c ON (c.id = a.cityId) " +
			"JOIN Department d ON (d.id = c.departmentId) " +
			"JOIN Province p2 ON (p2.id = d.provinceId) " +
			"JOIN Country c2 ON (c2.id = p2.countryId) " +
			"LEFT JOIN GeographicallyLocatedPatient glp ON (glp.patientId = p.id) " +
			"WHERE (a.latitude IS NULL OR a.longitude IS NULL) " +
			"AND a.street IS NOT NULL " +
			"AND a.number IS NOT NULL " +
			"AND (glp.patientId IS NULL OR glp.statusId = 1)")
	Page<FetchGlobalCoordinatesSanitaryResponsibilityAreaPatientAddressBo> fetchPatientWithNoGlobalCoordinates(Pageable pageable);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.sanitaryresponsibilityarea.domain.SanitaryRegionPatientMapCoordinatesBo(p.id, a.latitude, a.longitude) " +
			"FROM HospitalAudit ha " +
			"JOIN PatientAudit pa ON (pa.pk.hospitalAuditId = ha.id) " +
			"JOIN Patient p ON (p.id = pa.pk.patientId) " +
			"JOIN PersonExtended pe ON (pe.id = p.personId) " +
			"JOIN Address a ON (a.id = pe.addressId) " +
			"WHERE ha.actionType = 1 " +
			"AND ha.institutionId = :institutionId " +
			"AND a.latitude BETWEEN :lowerLatitude AND :upperLatitude " +
			"AND a.longitude BETWEEN :lowerLongitude AND :upperLongitude")
	List<SanitaryRegionPatientMapCoordinatesBo> fetchPatientCoordinatesByAddedInstitution(@Param("institutionId") Integer institutionId,
																						  @Param("lowerLatitude") Double lowerLatitude,
																						  @Param("lowerLongitude") Double lowerLongitude,
																						  @Param("upperLatitude") Double upperLatitude,
																						  @Param("upperLongitude") Double upperLongitude);

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT NEW net.pladema.sanitaryresponsibilityarea.domain.SanitaryRegionPatientMapCoordinatesBo(p.id, a.latitude, a.longitude) " +
			"FROM Patient p " +
			"JOIN OutpatientConsultation oc ON (oc.patientId = p.id) " +
			"JOIN PersonExtended pe ON (pe.id = p.personId) " +
			"JOIN Address a ON (a.id = pe.addressId) " +
			"WHERE oc.institutionId = :institutionId " +
			"AND oc.startDate BETWEEN :fromDate AND :toDate " +
			"AND a.latitude BETWEEN :lowerLatitude AND :upperLatitude " +
			"AND a.longitude BETWEEN :lowerLongitude AND :upperLongitude")
	List<SanitaryRegionPatientMapCoordinatesBo> fetchPatientCoordinatesByOutpatientConsultation(@Param("institutionId") Integer institutionId,
																								@Param("fromDate") LocalDate fromDate,
																								@Param("toDate") LocalDate toDate,
																								@Param("lowerLatitude") Double lowerLatitude,
																								@Param("lowerLongitude") Double lowerLongitude,
																								@Param("upperLatitude") Double upperLatitude,
																								@Param("upperLongitude") Double upperLongitude);

	@Transactional(readOnly = true)
	@Query(value = " SELECT NEW net.pladema.emergencycare.domain.EmergencyCarePatientBo(p.id, p.typeId, pe, petd.nameSelfDetermination, it.description) "+
			" FROM Patient p "+
			" LEFT JOIN Person pe ON (p.personId = pe.id) " +
			" LEFT JOIN PersonExtended petd ON (pe.id = petd.id) "+
			" LEFT JOIN IdentificationType it ON (pe.identificationTypeId = it.id) " +
			" WHERE p.id = :id ")
	EmergencyCarePatientBo findEmergencyCarePatientById(@Param("id") Integer id);

	@Transactional(readOnly = true)
	@Query("SELECT NEW ar.lamansys.sgh.shared.domain.medicationrequestvalidation.MedicationRequestValidationDispatcherPatientBo(p2.firstName, p2.lastName, it.description, p2.identificationNumber) " +
			"FROM Patient p " +
			"JOIN Person p2 ON (p2.id = p.personId) " +
			"JOIN IdentificationType it ON (it.id = p2.identificationTypeId) " +
			"WHERE p.id = :patientId")
    MedicationRequestValidationDispatcherPatientBo fetchPatientDataNeededForMedicationRequestValidation(@Param("patientId") Integer patientId);

}