package net.pladema.clinichistory.hospitalization.service.surgicalreport;

import java.util.Collections;
import java.util.Objects;

import javax.validation.ConstraintViolationException;

import ar.lamansys.sgh.clinichistory.application.document.validators.GeneralDocumentValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.documents.validation.InternmentDocumentValidator;
import net.pladema.clinichistory.hospitalization.service.domain.SurgicalReportBo;

@Component
public class SurgicalReportValidator extends InternmentDocumentValidator {

	private final InternmentEpisodeService internmentEpisodeService;

	public SurgicalReportValidator(InternmentEpisodeService internmentEpisodeService,
								   GeneralDocumentValidator generalDocumentValidator) {
		super(generalDocumentValidator);
		this.internmentEpisodeService = internmentEpisodeService;
	}

	public void assertHasAnamnesis(Integer internmentEpisodeId) {
		if (!internmentEpisodeService.haveAnamnesis(internmentEpisodeId)) {
			throw new ConstraintViolationException("La internación no posee una evaluación de ingreso", Collections.emptySet());
		}
	}

	public void assertSurgicalReportValid(SurgicalReportBo surgicalReport) {
		surgicalReport.validateSelf();
		super.assertDocumentValid(surgicalReport);
	}

	public void assertProsthesisValid(SurgicalReportBo surgicalReport) {
		if (Objects.isNull(surgicalReport.getProsthesisInfo()))
			return;
		if (surgicalReport.getProsthesisInfo().getHasProsthesis()){
			if (StringUtils.isEmpty(surgicalReport.getProsthesisInfo().getDescription()))
				throw new ConstraintViolationException("Si el parte quirúrgico tiene prótesis, la descripción no puede ser vacía ",Collections.emptySet());
		} else {
			if (StringUtils.isNotEmpty(surgicalReport.getProsthesisInfo().getDescription()))
				throw new ConstraintViolationException("Si el parte quirúrgico no tiene prótesis, la descripción debe ser vacía ",Collections.emptySet());
		}
	}
}
