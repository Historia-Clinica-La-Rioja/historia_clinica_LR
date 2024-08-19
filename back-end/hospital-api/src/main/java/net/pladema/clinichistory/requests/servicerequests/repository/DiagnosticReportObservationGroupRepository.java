package net.pladema.clinichistory.requests.servicerequests.repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReport;
import net.pladema.clinichistory.requests.servicerequests.repository.domain.DiagnosticReportObservationVo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.pladema.clinichistory.requests.servicerequests.repository.entity.DiagnosticReportObservationGroup;

import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DiagnosticReportObservationGroupRepository extends JpaRepository<DiagnosticReportObservationGroup, Integer> {
	Optional<DiagnosticReportObservationGroup> findByDiagnosticReportIdAndProcedureTemplateId(Integer diagnosticReportId, Integer procedureTemplateId);

	Optional<DiagnosticReportObservationGroup> findByDiagnosticReportId(Integer drId);

	@Query(value = "" +
	"SELECT NEW net.pladema.clinichistory.requests.servicerequests.repository.domain.DiagnosticReportObservationVo(  " +
	"	diagnosticReportObservationGroup.id,  " +
	"	diagnosticReportObservationGroup.diagnosticReportId,  " +
	"	diagnosticReportObservationGroup.procedureTemplateId,  " +
	"	diagnosticReportObservationGroup.isPartialUpload,  " +
	"	diagnosticReportObservation.id,  " +
	"	diagnosticReportObservation.procedureParameterId,  " +
	"	diagnosticReportObservation.value,  " +
	"	diagnosticReportObservation.unitOfMeasureId," +
	"   loincCode.description," +
	"   loincCode.displayName," +
	"   loincCode.customDisplayName," +
	"   procedureParameter.typeId," +
	"   unitOfMeasure.description" +
	")  " +
	"FROM  " +
	"	DiagnosticReportObservationGroup diagnosticReportObservationGroup  " +
	"	JOIN DiagnosticReportObservation diagnosticReportObservation ON  " +
	" 		(diagnosticReportObservation.diagnosticReportObservationGroupId = diagnosticReportObservationGroup.id)  " +
	"	JOIN ProcedureParameter procedureParameter ON  " +
	"		(diagnosticReportObservation.procedureParameterId = procedureParameter.id)  " +
	"	JOIN LoincCode loincCode ON  " +
	"		(procedureParameter.loincId = loincCode.id)  " +
	"	LEFT JOIN UnitOfMeasure unitOfMeasure ON  " +
	"		(diagnosticReportObservation.unitOfMeasureId IS NOT NULL AND diagnosticReportObservation.unitOfMeasureId = unitOfMeasure.id) " +
	"WHERE  " +
	"	diagnosticReportObservationGroup.diagnosticReportId IN :diagnosticReportIds" +
	"")
    List<DiagnosticReportObservationVo> _findGroupAndObservationsByDiagnosticReportIds(@Param("diagnosticReportIds") List<Integer> diagnosticReportIds);

	//Returns a list of lists [(id, sctid, pt), (id, sctid, pt)...]
	@Query("SELECT new list(snomed.id, snomed.sctid, snomed.pt) FROM Snomed snomed WHERE snomed.id IN :snomedIds")
	List<List<String>> _fetchSnomeds(@Param("snomedIds") List<Integer> snomedIds);

	default List<DiagnosticReportObservationVo> findGroupAndObservationsByDiagnosticReportIds(List<Integer> diagnosticReportIds){
		var observations =  _findGroupAndObservationsByDiagnosticReportIds(diagnosticReportIds);
		//The join between the observations and the snomed table isn't easy to resolve in a single query. The observation
		//value is a string and the snomed.id column is an integer.
		//That's why we must look up the snomeds in a separate query and then match them with the observations.

		var snomedIds = getDrSnomedIds(observations);
		var snomeds =  _fetchSnomeds(snomedIds);

		//For each observation try to set the proper snomed pt and sctid
		observations.forEach(observation -> {
			if (observation.isSnomed()){
				try {
					var snomedId = Integer.parseInt(observation.getValue());
					snomeds.stream()
					.filter(x -> Objects.equals(x.get(0), snomedId))
					.findFirst()
					.ifPresent(foundSnomed -> observation.setSnomedValues(foundSnomed.get(1), foundSnomed.get(2)));
				} catch (NumberFormatException e) {}
			}
		});

		return observations;
	}

	private static List<Integer> getDrSnomedIds(List<DiagnosticReportObservationVo> ret) {
		return ret.stream().filter(DiagnosticReportObservationVo::isSnomed).map(DiagnosticReportObservationVo::getValue).map(value -> {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException e) {
				return null;
			}
		}).filter(Objects::nonNull).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@Query("SELECT dr FROM DiagnosticReport dr " +
	 "	JOIN DocumentDiagnosticReport ddr ON (ddr.pk.diagnosticReportId = dr.id)  " +
	 "	JOIN Document d ON (d.id = ddr.pk.documentId) " +
	 "	JOIN ServiceRequest sr ON (sr.id = d.sourceId AND d.sourceTypeId = " + SourceType.ORDER + " ) " +
	 "WHERE sr.id = (" +
	 "	SELECT sr2.id " +
	 "  FROM DiagnosticReport dr2 " +
	 "  JOIN DocumentDiagnosticReport ddr2 ON (ddr2.pk.diagnosticReportId = dr2.id) " +
	 "  JOIN Document d2 ON (d2.id = ddr2.pk.documentId) " +
	 "  JOIN ServiceRequest sr2 ON (sr2.id = d2.sourceId AND d2.sourceTypeId = " + SourceType.ORDER + ") " +
	 "  WHERE dr2.id = :diagnosticReportId " +
	 ")"
	)
    List<DiagnosticReport> findRelatedDiagnosticReports(@Param("diagnosticReportId") Integer diagnosticReportId);

}
