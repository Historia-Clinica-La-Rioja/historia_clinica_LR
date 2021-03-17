package net.pladema.patient.repository;

import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.service.domain.PatientSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer>, PatientRepositoryCustom, PatientRepositorySearch {

	@Query(value = " SELECT new net.pladema.patient.service.domain.PatientSearch(person, patient.id, patientType.active, 0) " +
			"	FROM Patient patient JOIN Person person ON patient.personId = person.id "
			+ " JOIN PatientType patientType ON patient.typeId = patientType.id" +
			" WHERE person.firstName = :name " +
			" OR person.lastName = :lastName " +
			" OR person.identificationNumber = :identificationNumber " +
			" OR person.birthDate = :birthDate ")
	public Stream<PatientSearch> getAllByFilter(@Param("name") String name, @Param("lastName") String lastName,
												@Param("identificationNumber") String identificationNumber, @Param("birthDate") LocalDate birthDate);

	@Query(value = " SELECT p.id " +
			"FROM User u " +
			"LEFT JOIN Patient p ON (p.personId = u.personId) " +
			"WHERE u.id = :userId")
	public Optional<Integer> getPatientIdByUser(@Param("userId") Integer userId);

	@Query(value = " SELECT u.personId " +
			"FROM User u " +
			"WHERE u.id = :userId")
	public Optional<Integer> getPersonIdByUser(@Param("userId") Integer userId);
}