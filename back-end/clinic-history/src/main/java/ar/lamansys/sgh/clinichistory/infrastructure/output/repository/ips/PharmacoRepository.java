
package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.domain.ips.IndicationMinimalBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.Pharmaco;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PharmacoRepository extends JpaRepository<Pharmaco, Integer> {
	
	@Transactional(readOnly = true)
	@Query(value = "SELECT p "
			+ "FROM Pharmaco p "
			+ "JOIN Indication i ON p.id = i.id "
			+ "JOIN DocumentIndication di ON di.pk.indicationId = i.id "
			+ "JOIN Document doc ON di.pk.documentId = doc.id "
			+ "WHERE doc.sourceId = :internmentEpisodeId "
			+ "AND doc.typeId = :documentTypeId "
			+ "AND i.sourceTypeId = :sourceTypeId "
			+ "ORDER BY i.creationable.createdOn DESC")
	List<Pharmaco> getByInternmentEpisodeId(@Param("internmentEpisodeId") Integer internmentEpisodeId,
											@Param("documentTypeId") Short documentTypeId,
											@Param("sourceTypeId") Short sourceTypeId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT NEW ar.lamansys.sgh.clinichistory.domain.ips.IndicationMinimalBo(p.snomedId, q.value, q.unit, p.viaId, count(*) AS total) "
			+ "FROM Pharmaco p "
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

	@Transactional(readOnly = true)
	@Query(value = "SELECT d.periodUnit, count(*) AS total "
			+ "FROM Pharmaco p "
			+ "JOIN Dosage d ON (d.id = p.dosageId AND p.snomedId = :snomedId AND p.viaId = :viaId) "
			+ "JOIN Quantity q ON (q.id = d.doseQuantityId AND q.value = :value AND q.unit = :unit) "
			+ "GROUP BY d.periodUnit "
			+ "ORDER BY total DESC")
	List<Object[]> getMostPeriodUnitIndicated(@Param("snomedId") Integer snomedId,
											 @Param("value") Double value,
											 @Param("unit") String unit,
											 @Param("viaId") Short viaId,
											 Pageable page);

	@Transactional(readOnly = true)
	@Query(value = "SELECT cast(d.startDate as LocalTime), count(*) AS total "
			+ "FROM Pharmaco p "
			+ "JOIN Dosage d ON (d.id = p.dosageId AND p.snomedId = :snomedId AND p.viaId = :viaId AND d.periodUnit = :periodUnit) "
			+ "JOIN Quantity q ON (q.id = d.doseQuantityId AND q.value = :value AND q.unit = :unit) "
			+ "GROUP BY cast(d.startDate as LocalTime) "
			+ "ORDER BY total DESC")
	List<Object[]> getMostStartDateTimeIndicated(@Param("snomedId") Integer snomedId,
												 @Param("value") Double value,
												 @Param("unit") String unit,
												 @Param("viaId") Short viaId,
												 @Param("periodUnit") String periodUnit,
												 Pageable page);

	@Transactional(readOnly = true)
	@Query(value = "SELECT d.frequency, count(*) AS total "
			+ "FROM Pharmaco p "
			+ "JOIN Dosage d ON (d.id = p.dosageId AND p.snomedId = :snomedId AND p.viaId = :viaId AND d.periodUnit = 'h') "
			+ "JOIN Quantity q ON (q.id = d.doseQuantityId AND q.value = :value AND q.unit = :unit) "
			+ "GROUP BY d.frequency "
			+ "ORDER BY total DESC")
	List<Object[]> getMostFrequencyIndicated(@Param("snomedId") Integer snomedId,
											 @Param("value") Double value,
											 @Param("unit") String unit,
											 @Param("viaId") Short viaId,
											 Pageable page);
}
