package net.pladema.snvs.infrastructure.output.repository.snvs;

import net.pladema.snvs.infrastructure.output.repository.snvs.entity.ManualClassification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManualClassificationRepository extends JpaRepository<ManualClassification, Integer> {

	@Query("SELECT DISTINCT mc " +
			"FROM Snomed s " +
			"JOIN SnomedRelatedGroup srg ON (s.id = srg.snomedId) " +
			"JOIN SnvsGroup ng ON (ng.groupId = srg.groupId) " +
			"JOIN ManualClassification mc ON (mc.id = ng.manualClassificationId) " +
			"WHERE sctid = :sctid " +
			"AND pt = :pt")
	List<ManualClassification> isSnvsReportable(@Param("sctid") String sctid, @Param("pt") String pt);
}
