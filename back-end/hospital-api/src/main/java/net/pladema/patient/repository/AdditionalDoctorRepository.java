package net.pladema.patient.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.patient.repository.entity.AdditionalDoctor;

@Repository
public interface AdditionalDoctorRepository extends JpaRepository<AdditionalDoctor, Integer> {
	
	@Transactional(readOnly = true)
    @Query("SELECT ad " +
            "FROM AdditionalDoctor AS ad " +
            "WHERE ad.patientId = :patientId ")
    Collection<AdditionalDoctor> getAdditionalDoctors(@Param("patientId") Integer patientId);
	
}
