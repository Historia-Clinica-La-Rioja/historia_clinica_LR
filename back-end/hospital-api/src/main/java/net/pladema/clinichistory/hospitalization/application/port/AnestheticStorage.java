package net.pladema.clinichistory.hospitalization.application.port;

import java.util.Optional;
import net.pladema.clinichistory.hospitalization.domain.AnestheticReportBo;

public interface AnestheticStorage {
    Integer save(AnestheticReportBo anestheticReport);

    Optional<AnestheticReportBo> get(Long documentId);
}
