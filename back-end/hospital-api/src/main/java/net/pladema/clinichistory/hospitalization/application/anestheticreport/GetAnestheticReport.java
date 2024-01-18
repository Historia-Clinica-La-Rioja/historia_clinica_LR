package net.pladema.clinichistory.hospitalization.application.anestheticreport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.exceptions.AnestheticReportException;
import net.pladema.clinichistory.hospitalization.application.port.AnestheticStorage;
import net.pladema.clinichistory.hospitalization.domain.AnestheticReportBo;
import net.pladema.clinichistory.hospitalization.domain.exceptions.AnestheticReportEnumException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetAnestheticReport {

    private final AnestheticStorage anestheticStorage;

    public AnestheticReportBo run(Long documentId) {
        log.debug("Input parameters -> documentId {}", documentId);

        AnestheticReportBo result = anestheticStorage.get(documentId)
                .orElseThrow(()-> new AnestheticReportException(
                        AnestheticReportEnumException.ANESTHETIC_REPORT_NOT_FOUND,
                        "anesthetic-report.not-found"));

        log.debug("Output -> anestheticReport {}", result);
        return result;
    }
}
