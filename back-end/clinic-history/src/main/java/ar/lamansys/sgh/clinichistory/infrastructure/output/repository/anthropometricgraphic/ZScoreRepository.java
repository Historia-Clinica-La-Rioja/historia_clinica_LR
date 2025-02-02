package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.anthropometricgraphic;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.anthropometricgraphic.entity.ZScore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ZScoreRepository extends JpaRepository<ZScore, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT zs " +
			"FROM ZScore zs " +
			"WHERE zs.anthropometricGraphicId = :anthropometricGraphicId " +
			"AND zs.genderId = :genderId " +
			"AND zs.timePeriodId = :timePeriodId")
	List<ZScore> getByGraphicIdAndGenderIdAndTimePeriodId(@Param("anthropometricGraphicId")Short anthropometricGraphicId, @Param("genderId")Short genderId, @Param("timePeriodId")Short timePeriodId);

}
