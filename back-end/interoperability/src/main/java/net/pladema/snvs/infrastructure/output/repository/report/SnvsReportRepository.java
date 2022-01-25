package net.pladema.snvs.infrastructure.output.repository.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SnvsReportRepository extends JpaRepository<SnvsReport, Integer> {

	@Transactional(readOnly = true)
	@Query(value = " SELECT s FROM SnvsReport s "
			+ "WHERE s.institutionId =:institutionId")
	List<SnvsReport> getAllByInstitution(@Param("institutionId") Integer institutionId);


	@Transactional(readOnly = true)
	@Query(value = " SELECT s FROM SnvsReport s "
			+ " WHERE s.id = :reportId AND s.institutionId =:institutionId")
	List<SnvsReport> getAllByReportAndInstitution(@Param("reportId") Integer reportId,
										 @Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT s.institutionId FROM SnvsReport s " +
			"WHERE s.id = :id ")
	Integer getInstitutionId(@Param("id") Integer id);

	@Transactional(readOnly = true)
	@Query(value = "SELECT s.id FROM SnvsReport s")
	List<Integer> getAllIds();

	@Transactional(readOnly = true)
	@Query(value = "SELECT s.id FROM SnvsReport s " +
			"WHERE s.institutionId IN :institutionsIds")
	List<Integer> getAllIdsByInstitutionsId(@Param("institutionsIds") List<Integer> institutionsIds);

	@Transactional(readOnly = true)
	@Query(value = "SELECT s FROM SnvsReport s " +
			"WHERE s.institutionId IN :institutionsIds")
	List<SnvsReport> getAllReportsByInstitutions(List<Integer> allowedInstitutions);

}
