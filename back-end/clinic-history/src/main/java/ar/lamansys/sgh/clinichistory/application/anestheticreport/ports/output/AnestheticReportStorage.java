package ar.lamansys.sgh.clinichistory.application.anestheticreport.ports.output;

import java.util.Optional;

import ar.lamansys.sgh.clinichistory.domain.document.impl.AnestheticReportBo;

public interface AnestheticReportStorage {
    Integer save(AnestheticReportBo anestheticReport);

    Integer updateDocumentId(AnestheticReportBo anestheticReport);

    Optional<AnestheticReportBo> get(Long documentId);

    Long getDocumentIdFromLastAnestheticReportDraft(Integer internmentEpisodeId);

    String getAntestheticChart(Long documentId);
}
