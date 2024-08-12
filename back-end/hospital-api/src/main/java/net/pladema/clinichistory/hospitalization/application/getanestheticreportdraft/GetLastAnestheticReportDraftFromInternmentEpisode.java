package net.pladema.clinichistory.hospitalization.application.getanestheticreportdraft;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.port.InternmentEpisodeStorage;
import net.pladema.clinichistory.hospitalization.service.domain.AnestheticReportSummaryBo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetLastAnestheticReportDraftFromInternmentEpisode {

    private final InternmentEpisodeStorage internmentEpisodeStorage;

    public Optional<AnestheticReportSummaryBo> run(Integer internmentEpisodeId) {
        log.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        Long documentId = internmentEpisodeStorage.getDocumentIdFromLastAnestheticReportDraft(internmentEpisodeId);
        Optional<AnestheticReportSummaryBo> result = Optional.of(new AnestheticReportSummaryBo(documentId, DocumentStatus.DRAFT));
        log.debug("Output -> {}", result);
        return result;
    }
}
