package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;

import org.springframework.stereotype.Repository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProceduresStatus;

import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class HCHProcedureRepository {

	public static final String OUTPUT = "Output -> {}";

	private final EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<ProcedureVo> findGeneralState(Integer internmentEpisodeId, List<Short> invalidDocumentTypes) {
		log.debug("Input parameters -> internmentEpisodeId {} invalidDocumentTypes {}", internmentEpisodeId, invalidDocumentTypes);

		String invalidDocumentCondition = (invalidDocumentTypes.isEmpty()) ? "" : "AND d.type_id NOT IN :invalidDocumentTypes ";

		String sqlString = "with temporal as (" +
		"select distinct " +
		"i.id, " +
		"i.snomed_id, " +
		"i.status_id, " +
		"i.performed_date, " +
		"i.updated_on, " +
		"i.procedure_type_id, " +
		"row_number() over (partition by i.snomed_id, i.performed_date order by i.updated_on desc) as rw " +
		"from {h-schema}document d " +
		"join {h-schema}document_procedure di on (d.id = di.document_id) " +
		"join {h-schema}procedures i on (di.procedure_id = i.id) " +
		"where d.source_id = :internmentEpisodeId " +
		" and d.source_type_id = " + SourceType.HOSPITALIZATION +
		" and d.status_id IN (:documentStatusId) " +
			 invalidDocumentCondition +
		") " +
		"select t.id as id, s.sctid as sctid, s.pt, t.status_id, t.performed_date, t.procedure_type_id " +
		"from temporal t " +
		"join {h-schema}snomed s on t.snomed_id = s.id " +
		"where rw = 1 and not status_id = :procedureStatusId " +
		"order by t.updated_on";

		Query query = entityManager.createNativeQuery(sqlString)
				.setParameter("internmentEpisodeId", internmentEpisodeId)
				.setParameter("documentStatusId", List.of(DocumentStatus.FINAL, DocumentStatus.DRAFT))
				.setParameter("procedureStatusId", ProceduresStatus.ERROR);
		if (!invalidDocumentTypes.isEmpty())
			query.setParameter("invalidDocumentTypes", invalidDocumentTypes);

		List<Object[]> queryResult = query.getResultList();

		List<ProcedureVo> result = new ArrayList<>();
		queryResult.forEach(i -> {
			Date date = (Date) i[4];
			result.add(new ProcedureVo(
					(Integer) i[0],
					new Snomed((String) i[1], (String) i[2], null, null),
					(String) i[3],
					date != null ? date.toLocalDate() : null,
					(Short) i[5]));
		});
		log.debug(OUTPUT, result);
		return result;
	}
}
