package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportTreeBo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReportTree;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReportTreePK;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiagnosticReportTreeRepository extends JpaRepository<DiagnosticReportTree, DiagnosticReportTreePK> {

	@Query("SELECT " +
	 "dr.pk.diagnosticReportParentId " +
	 "FROM DiagnosticReportTree dr " +
	  "WHERE dr.pk.diagnosticReportChildId = :parentId")
	Optional<Integer> findRoot(@Param("parentId") Integer parentId);

	@Query("SELECT " +
		"NEW ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportTreeBo(" +
		 "drt.pk.diagnosticReportParentId, drt.pk.diagnosticReportChildId) " +
		"FROM DiagnosticReportTree drt " +
		"WHERE drt.pk.diagnosticReportChildId IN :diagnosticReportIds")
	List<DiagnosticReportTreeBo> findRoots(@Param("diagnosticReportIds") List<Integer> diagnosticReportIds);
}
