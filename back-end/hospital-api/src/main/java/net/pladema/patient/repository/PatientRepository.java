package net.pladema.patient.repository;

import net.pladema.patient.repository.domain.PatientPersonVo;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.service.domain.PatientSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer>, PatientRepositoryCustom, PatientRepositorySearch {

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
			"WHERE patient.typeId = :patientTypeId ")
	List<PatientPersonVo> getAllByPatientType(@Param("patientTypeId") Short patientTypeId);

	@Query(value = " SELECT p.identificationNumber " +
			"FROM Patient pa " +
			"JOIN Person p ON p.id = pa.personId " +
			"WHERE pa.id = :patientId")
	Optional<String> getIdentificationNumber(@Param("patientId") Integer patientId);

}