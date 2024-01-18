package net.pladema.clinichistory.hospitalization.service.documents.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.domain.AnestheticReportBo;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AnestheticReportValidator extends InternmentDocumentValidator {

    public void assertContextValid(AnestheticReportBo anestheticReport) {

        super.assertContextValid(anestheticReport);
        super.assertDocumentValid(anestheticReport);

        log.trace("Output -> isValid anestheticReport {}", anestheticReport);
    }
}
