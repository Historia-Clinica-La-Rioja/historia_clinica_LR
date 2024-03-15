package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.anthropometricgraphic;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.anthropometricgraphic.entity.Percentiles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PercentilesRepository extends JpaRepository<Percentiles, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT p " +
			"FROM Percentiles p " +
			"WHERE p.anthropometricGraphicId = :anthropometricGraphicId " +
			"AND p.genderId = :genderId " +
			"AND p.timePeriodId = :timePeriodId")
	List<Percentiles> getByGraphicIdAndGenderIdAndTimePeriodId(@Param("anthropometricGraphicId")Short anthropometricGraphicId, @Param("genderId")Short genderId, @Param("timePeriodId")Short timePeriodId);

}
