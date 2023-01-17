package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationRiskFactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ObservationRiskFactorRepository extends JpaRepository<ObservationRiskFactor, Integer> {

	@Transactional
	@Modifying
	@Query("UPDATE ObservationRiskFactor orf " +
			"SET orf.patientId = :newPatientId " +
			"WHERE orf.id IN :orfIds")
	void updatePatient(@Param("orfIds")List<Integer> orfIds, @Param("newPatientId") Integer newPatientId);

}
