package net.pladema.clinichistory.hospitalization.application.port;

import net.pladema.clinichistory.hospitalization.domain.InternmentEpisodeBo;
import net.pladema.clinichistory.hospitalization.repository.domain.summary.InternmentSummaryVo;

import java.util.Optional;

public interface InternmentEpisodeStorage {
    Integer save(InternmentEpisodeBo internmentEpisodeBo);

    boolean hasIntermentEpisodeActiveInInstitution(Integer patientId, Integer institutionId);

    Optional<InternmentSummaryVo> getSummary(Integer internmentEpisodeId);

    Long getDocumentIdFromLastAnestheticReportDraft(Integer internmentEpisodeId);
}
