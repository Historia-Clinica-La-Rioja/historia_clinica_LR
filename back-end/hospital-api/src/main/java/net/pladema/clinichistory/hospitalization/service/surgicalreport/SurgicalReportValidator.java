package net.pladema.clinichistory.hospitalization.service.surgicalreport;

import java.util.Collections;

import javax.validation.ConstraintViolationException;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.documents.validation.InternmentDocumentValidator;
import net.pladema.clinichistory.hospitalization.service.domain.SurgicalReportBo;

@Component
@RequiredArgsConstructor
public class SurgicalReportValidator extends InternmentDocumentValidator {

	private final InternmentEpisodeService internmentEpisodeService;

	public void assertHasAnamnesis(Integer internmentEpisodeId) {
		if (!internmentEpisodeService.haveAnamnesis(internmentEpisodeId)) {
			throw new ConstraintViolationException("La internación no posee una evaluación de ingreso", Collections.emptySet());
		}
	}

	public void assertSurgicalReportValid(SurgicalReportBo surgicalReport) {
		surgicalReport.validateSelf();
		super.assertDocumentValid(surgicalReport);
	}

}
