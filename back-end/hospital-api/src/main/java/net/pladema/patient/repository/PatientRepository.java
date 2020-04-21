package net.pladema.patient.repository;

import net.pladema.patient.repository.domain.BasicListedPatient;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.person.repository.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

	@Query(value = " SELECT person " + 
		 "	FROM Patient patient JOIN Person person ON patient.personId = person.id " +
		 " WHERE person.firstName = :name " +
		 " OR person.lastName = :lastName " +
		 " OR person.identificationNumber = :identificationNumber " +
		 " OR person.birthDate = :birthDate ")
	public Stream<Person> getAllByFilter(@Param("name") String name, @Param("lastName") String lastName,
			@Param("identificationNumber") String identificationNumber, @Param("birthDate") LocalDate birthDate);


	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.patient.repository.domain.BasicListedPatient(pa.id, pe.identificationTypeId, " +
			"pe.identificationNumber, pe.firstName, pe.lastName, pe.birthDate, pe.genderId) " +
			" FROM Person as pe JOIN Patient as pa ON (pe.id = pa.personId) ")
	public List<BasicListedPatient> findAllPatientsListedData();

}
