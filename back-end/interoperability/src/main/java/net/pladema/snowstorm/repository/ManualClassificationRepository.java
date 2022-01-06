package net.pladema.snowstorm.repository;

import net.pladema.snowstorm.repository.entity.ManualClassification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManualClassificationRepository extends JpaRepository<ManualClassification, Integer> {

	@Query("SELECT DISTINCT mc " +
			"FROM Snomed s, " +
			"SnomedRelatedGroup srg, " +
			"SnomedGroup sg, " +
			"SnvsGroup ng, " +
			"ManualClassification mc " +
			"WHERE sctid = :sctid AND pt = :pt " +
			"AND srg.snomedId = s.id " +
			"AND srg.groupId = sg.id " +
			"AND ng.groupId = sg.id " +
			"AND ng.manualClassificationId = mc.id")
	List<ManualClassification> isSnvsReportable(@Param("sctid") String sctid, @Param("pt") String pt);
}
