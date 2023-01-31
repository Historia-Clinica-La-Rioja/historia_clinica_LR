package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.domain.ips.IndicationMinimalBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.ParenteralPlan;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ParenteralPlanRepository extends JpaRepository<ParenteralPlan, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT pp "
			+ "FROM ParenteralPlan pp "
			+ "JOIN Indication i ON i.id = pp.id "
			+ "JOIN DocumentIndication di ON di.pk.indicationId = i.id "
			+ "JOIN Document doc ON di.pk.documentId = doc.id "
			+ "WHERE doc.sourceId = :internmentEpisodeId "
			+ "AND doc.typeId = :documentTypeId "
			+ "ORDER BY i.creationable.createdOn DESC")
	List<ParenteralPlan> getByInternmentEpisodeId(@Param("internmentEpisodeId") Integer internmentEpisodeId,
												   @Param("documentTypeId") Short documentTypeId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT NEW ar.lamansys.sgh.clinichistory.domain.ips.IndicationMinimalBo(p.snomedId, q.value, q.unit, p.viaId, count(*) AS total) "
			+ "FROM ParenteralPlan p "
			+ "JOIN Indication i ON (p.id = i.id AND p.professionalId = :professionalId) "
			+ "JOIN DocumentIndication di ON (di.pk.indicationId = i.id) "
			+ "JOIN Document doc ON (di.pk.documentId = doc.id AND doc.institutionId = :institutionId) "
			+ "JOIN Dosage d ON (d.id = p.dosageId) "
			+ "JOIN Quantity q ON (q.id = d.doseQuantityId) "
			+ "GROUP BY p.snomedId, q.value, q.unit, p.viaId "
			+ "ORDER BY total DESC")
	List<IndicationMinimalBo> getMostFrequent(@Param("professionalId") Integer professionalId,
											  @Param("institutionId") Integer institutionId,
											  Pageable page);

}
