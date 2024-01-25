package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.MedicationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.MedicationStatementStatus;
import javax.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class HCHMedicationStatementRepositoryImpl implements HCHMedicationStatementRepository {

    public static final String OUTPUT = "Output -> {}";

    private final EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<MedicationVo> findGeneralState(Integer internmentEpisodeId, List<Short> invalidDocumentTypes) {
        log.debug("Input parameters -> internmentEpisodeId {} invalidDocumentTypes {}", internmentEpisodeId, invalidDocumentTypes);

        String invalidDocumentCondition = (invalidDocumentTypes.isEmpty()) ? "" : "AND d.type_id NOT IN :invalidDocumentTypes ";

        String sqlString = "with temporal as (" +
                "select distinct " +
                "ms.id, " +
                "ms.snomed_id, " +
                "ms.status_id, " +
                "ms.note_id, " +
                "ms.updated_on, " +
                "row_number() over (partition by ms.snomed_id order by ms.updated_on desc) as rw " +
                "from {h-schema}document d " +
                "join {h-schema}document_medicamention_statement dms on d.id = dms.document_id " +
                "join {h-schema}medication_statement ms on dms.medication_statement_id = ms.id " +
                "where d.source_id = :internmentEpisodeId " +
                "and d.source_type_id = " + SourceType.HOSPITALIZATION +" "+
                "and d.status_id IN (:documentStatusId) " +
                     invalidDocumentCondition +
                ") " +
                "select t.id as id, s.sctid as sctid, s.pt, status_id, n.id as note_id, n.description as note " +
                "from temporal t " +
                "left join {h-schema}note n on t.note_id = n.id " +
                "join {h-schema}snomed s on t.snomed_id = s.id " +
                "where rw = 1 and not status_id = :medicationStatusId " +
                "order by t.updated_on";

        Query query = entityManager.createNativeQuery(sqlString)
                .setParameter("internmentEpisodeId", internmentEpisodeId)
                .setParameter("documentStatusId", List.of(DocumentStatus.FINAL, DocumentStatus.DRAFT))
                .setParameter("medicationStatusId", MedicationStatementStatus.ERROR);
        if (!invalidDocumentTypes.isEmpty())
            query.setParameter("invalidDocumentTypes", invalidDocumentTypes);

        List<Object[]> queryResult = query.getResultList();

        List<MedicationVo> result = new ArrayList<>();
        queryResult.forEach(m -> 
            result.add(new MedicationVo(
                    (Integer)m[0],
                    new Snomed((String)m[1], (String)m[2], null, null),
                    (String)m[3],
                    m[4] != null ? ((BigInteger)m[4]).longValue() : null,
                    (String)m[5]
            ))
        );
        log.debug(OUTPUT, result);
        return result;
    }
}
