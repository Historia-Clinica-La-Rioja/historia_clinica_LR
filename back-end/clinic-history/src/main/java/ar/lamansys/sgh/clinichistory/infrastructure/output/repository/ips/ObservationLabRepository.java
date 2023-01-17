package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationLab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ObservationLabRepository extends JpaRepository<ObservationLab, Integer>{

	@Transactional
	@Modifying
	@Query("UPDATE ObservationLab AS ol " +
			"SET ol.patientId = :newPatientId " +
			"WHERE ol.id IN :olIds")
	void updatePatient(@Param("olIds")List<Integer> oldIds, @Param("newPatientId") Integer newPatientId);

}
